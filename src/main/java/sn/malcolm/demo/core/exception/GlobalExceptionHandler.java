package sn.malcolm.demo.core.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sn.malcolm.demo.core.payload.dto.Result;

import java.util.Arrays;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("Erreur de désérialisation JSON: {}", e.getMessage());

        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType() != null &&
                    invalidFormatException.getTargetType().isEnum()) {

                String fieldName = getFieldName(invalidFormatException);
                String invalidValue = invalidFormatException.getValue().toString();
                Class<? extends Enum> enumType = (Class<? extends Enum>) invalidFormatException.getTargetType();
                Enum[] enumConstants = enumType.getEnumConstants();
                String validValues = Arrays.toString(Arrays.stream(enumConstants).map(Enum::name).toArray());
                if (enumType.getSimpleName().equals("Role")) {
                    Map<String, String> roleDescriptions = Map.of(
                            "user", "Utilisateur standard avec accès limité",
                            "admin", "Administrateur avec accès complet"
                    );
                    StringBuilder detailedValidValues = new StringBuilder();
                    for (Enum constant : enumConstants) {
                        String description = roleDescriptions.getOrDefault(constant.name(), "Description non disponible");
                        detailedValidValues.append(String.format("%s (%s), ", constant.name(), description));
                    }
                    if (detailedValidValues.length() > 2) {
                        detailedValidValues.setLength(detailedValidValues.length() - 2);
                    }
                    String message = String.format("La valeur '%s' n'est pas valide pour le champ '%s'. Valeurs valides: %s",
                            invalidValue, fieldName, detailedValidValues);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(message));
                }

                String message = String.format("La valeur '%s' n'est pas valide pour le champ '%s'",
                        invalidValue, fieldName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(message));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error("Format de données invalide"));
    }

    private String getFieldName(InvalidFormatException e) {
        if (e.getPath() != null && !e.getPath().isEmpty()) {
            return e.getPath().get(e.getPath().size() - 1).getFieldName();
        }
        return "champ inconnu";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Accès refusé: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error("Vous n'avez pas les autorisations nécessaires pour cette action"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleGlobalException(Exception e) {
        log.error("Exception non gérée: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error("Une erreur interne est survenue"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Result<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entité non trouvée: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(errorMessage));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Result<Void>> handleApiException(ApiException e) {
        log.error("ApiException: {}", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(Result.error(e.getMessage()));
    }
}