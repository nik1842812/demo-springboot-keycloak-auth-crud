package sn.malcolm.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.malcolm.demo.core.payload.request.LoginRequest;
import sn.malcolm.demo.core.payload.response.TokenResponse;
import sn.malcolm.demo.service.KeycloakService;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Endpoints pour l'authentification des utilisateurs")
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakService keycloakService;

    @Operation(summary = "Authentification de l'utilisateur", description = "Authentifie un utilisateur et retourne un jeton JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentification réussie"),
            @ApiResponse(responseCode = "401", description = "Échec de l'authentification")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(keycloakService.authenticate(loginRequest));
    }

    @Operation(summary = "Rafraîchissement du jeton", description = "Rafraîchit le jeton JWT en utilisant un jeton de rafraîchissement.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rafraîchissement réussi"),
            @ApiResponse(responseCode = "401", description = "Jeton de rafraîchissement invalide")
        })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(keycloakService.refreshToken(refreshToken));
    }

    @Operation(summary = "Déconnexion de l'utilisateur", description = "Déconnecte un utilisateur en invalidant son jeton.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Déconnexion réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable String userId) {
        keycloakService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
