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
import sn.malcolm.demo.core.payload.dto.ApiResponseDTO;

import java.util.Arrays;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("Erreur de désérialisation JSON: {}", e.getMessage());

        // Vérifier si c'est une erreur d'enum invalide
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType() != null &&
                    invalidFormatException.getTargetType().isEnum()) {

                String fieldName = getFieldName(invalidFormatException);
                String invalidValue = invalidFormatException.getValue().toString();
                Class<? extends Enum> enumType = (Class<? extends Enum>) invalidFormatException.getTargetType();
                Enum[] enumConstants = enumType.getEnumConstants();
                String validValues = Arrays.toString(Arrays.stream(enumConstants).map(Enum::name).toArray());
                // Cas spécifique pour l'enum 'Role'
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
                    // Supprimer la dernière virgule et espace
                    if (detailedValidValues.length() > 2) {
                        detailedValidValues.setLength(detailedValidValues.length() - 2);
                    }
                    String message = String.format("La valeur '%s' n'est pas valide pour le champ '%s'. Valeurs valides: %s",
                            invalidValue, fieldName, detailedValidValues);
                    ApiResponseDTO response = new ApiResponseDTO(false, message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                // Cas général pour d'autres enums
                String message = String.format("La valeur '%s' n'est pas valide pour le champ '%s'",
                        invalidValue, fieldName);
                ApiResponseDTO response = new ApiResponseDTO(false, message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        ApiResponseDTO response = new ApiResponseDTO(false, "Format de données invalide");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private String getFieldName(InvalidFormatException e) {
        if (e.getPath() != null && !e.getPath().isEmpty()) {
            return e.getPath().get(e.getPath().size() - 1).getFieldName();
        }
        return "champ inconnu";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Accès refusé: {}", e.getMessage());
        ApiResponseDTO response = new ApiResponseDTO(false, "Vous n'avez pas les autorisations nécessaires pour cette action");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGlobalException(Exception e) {
        log.error("Exception non gérée: {}", e.getMessage(), e);
        ApiResponseDTO response = new ApiResponseDTO(false, "Une erreur interne est survenue");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entité non trouvée: {}", e.getMessage());
        ApiResponseDTO response = new ApiResponseDTO(false, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(false, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDTO);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDTO> handleApiException(ApiException e) {
        log.error("ApiException: {}", e.getMessage());
        ApiResponseDTO response = new ApiResponseDTO(false, e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(response);
    }
}
