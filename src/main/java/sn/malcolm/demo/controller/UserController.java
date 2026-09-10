package sn.malcolm.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.malcolm.demo.core.payload.dto.Result;
import sn.malcolm.demo.model.User;
import sn.malcolm.demo.service.impl.UserService;
import sn.malcolm.demo.view.UserView;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Créer un utilisateur", description = "Crée un nouvel utilisateur.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "409", description = "Conflit - L'utilisateur existe déjà")
    })
    @PostMapping
    public ResponseEntity<Result<User>> createUserody(@RequestBody @JsonView({UserView.UserWrite.class}) User user) {
        return ResponseEntity.ok(Result.success("Utilisateur créé avec succès", userService.createUser(user)));
    }

    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour les informations d'un utilisateur existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Result<User>> updateUser(@JsonView({UserView.UserWrite.class}) @PathVariable Integer id, @RequestBody @JsonView({UserView.UserWrite.class}) User user) {
        return ResponseEntity.ok(Result.success("Utilisateur mis à jour avec succès", userService.updateUser(id, user)));
    }

    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Result.success("Utilisateur supprimé avec succès", null));
    }

    @Operation(summary = "Récupérer un utilisateur par ID", description = "Retourne les informations d'un utilisateur spécifique en fonction de son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/{id}")
    @JsonView({UserView.UserReadDetail.class})
    public ResponseEntity<Result<User>> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(Result.success(userService.getUserById(id)));
    }

    @Operation(summary = "Récupérer tous les utilisateurs", description = "Retourne la liste de tous les utilisateurs.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès")
    })
    @GetMapping
    @JsonView({UserView.UserRead.class})
    public ResponseEntity<Result<List<User>>> getAllUsers() {
        return ResponseEntity.ok(Result.success(userService.getAllUsers()));
    }

    @Operation(summary = "Réinitialiser le mot de passe d'un utilisateur", description = "Réinitialise le mot de passe et envoie un e-mail de réinitialisation.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mot de passe réinitialisé et e-mail envoyé"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Result<Void>> resetUserPassword(@PathVariable Integer id,
                                                  @RequestParam String newPassword,
                                                  @RequestParam String resetLink,
                                                  @RequestParam int tokenDuration) {
        userService.resetUserPassword(id, newPassword, resetLink, tokenDuration);
        return ResponseEntity.ok(Result.success("Mot de passe réinitialisé et e-mail envoyé", null));
    }

    @Operation(summary = "Changer le mot de passe utilisateur", description = "Permet à l'utilisateur de changer son mot de passe en fournissant l'ancien et le nouveau.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mot de passe changé avec succès"),
        @ApiResponse(responseCode = "400", description = "Ancien mot de passe incorrect ou requête invalide"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Result<Void>> changeUserPassword(@PathVariable Integer id,
                                                  @RequestParam String oldPassword,
                                                  @RequestParam String newPassword) {
        userService.changeUserPassword(id, oldPassword, newPassword);
        return ResponseEntity.ok(Result.success("Mot de passe changé avec succès", null));
    }
}