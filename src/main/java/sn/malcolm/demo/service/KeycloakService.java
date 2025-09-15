package sn.malcolm.demo.service;


import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sn.malcolm.demo.core.exception.ApiException;
import sn.malcolm.demo.core.payload.request.LoginRequest;
import sn.malcolm.demo.core.payload.response.TokenResponse;
import sn.malcolm.demo.model.User;
import sn.malcolm.demo.repository.UserRepository;
import sn.malcolm.demo.security.UserSec;
import sn.malcolm.demo.security.service.AuthenticationSystem;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;

    private final UserRepository userRepository;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    // Authentifier un utilisateur et retourner le token
    public TokenResponse authenticate(LoginRequest loginRequest) {
        try {
            Keycloak keycloakAuth = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(loginRequest.getUsername())
                    .password(loginRequest.getPassword())
                    .build();
            AccessTokenResponse token = keycloakAuth.tokenManager().getAccessToken();
            return TokenResponse.builder()
                    .token(token.getToken())
                    .refreshToken(token.getRefreshToken())
                    .expiresIn(token.getExpiresIn())
                    .refreshExpiresIn(token.getRefreshExpiresIn())
                    .build();
        } catch (Exception e) {
            throw new ApiException("Identifiants invalides ou utilisateur inexistant", HttpStatus.UNAUTHORIZED);
        }
    }

    // Créer un nouvel utilisateur dans Keycloak
    @Transactional
    public String createUser(User user, String password) {
        try {
            UsersResource usersResource = keycloak.realm(realm).users();
            UserRepresentation userRep = new UserRepresentation();
            userRep.setUsername(user.getUsername());
            userRep.setEmail(user.getEmail());
            userRep.setFirstName(user.getFirstName());
            userRep.setLastName(user.getLastName());
            userRep.setEnabled(true);
            userRep.setRealmRoles(Collections.singletonList(String.valueOf(user.getRole())));

            Response response = usersResource.create(userRep);
            if (response.getStatus() != 201) {
                throw new ApiException("Erreur lors de la création de l'utilisateur Keycloak", HttpStatus.BAD_REQUEST);
            }
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            // Définir le mot de passe
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);
            usersResource.get(userId).resetPassword(passwordCred);
            return userId;
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la création de l'utilisateur Keycloak", HttpStatus.BAD_REQUEST);
        }
    }

    // Mettre à jour les informations d'un utilisateur Keycloak
    public void updateUser(String userId, User user) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation userRep = userResource.toRepresentation();
            userRep.setEmail(user.getEmail());
            userRep.setFirstName(user.getFirstName());
            userRep.setLastName(user.getLastName());
            userResource.update(userRep);
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la mise à jour de l'utilisateur", HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer un utilisateur Keycloak
    public void deleteUser(String userId) {
        try {
            keycloak.realm(realm).users().get(userId).remove();
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la suppression de l'utilisateur", HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer un utilisateur par son ID Keycloak
    public UserRepresentation getUserById(String userId) {
        try {
            return keycloak.realm(realm).users().get(userId).toRepresentation();
        } catch (Exception e) {
            throw new ApiException("Utilisateur introuvable", HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer un utilisateur par son username
    public UserRepresentation getUserByUsername(String username) {
        try {
            List<UserRepresentation> users = keycloak.realm(realm).users().search(username, true);
            if (users.isEmpty()) throw new ApiException("Utilisateur introuvable", HttpStatus.NOT_FOUND);
            return users.get(0);
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la recherche de l'utilisateur", HttpStatus.BAD_REQUEST);
        }
    }

    // Attribuer un rôle à un utilisateur
    public void assignRoleToUser(String userId, String roleName) {
        try {
            RealmResource realmResource = keycloak.realm(realm);
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
            realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
        } catch (Exception e) {
            throw new ApiException("Erreur lors de l'attribution du rôle", HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer un rôle d'un utilisateur
    public void removeRoleFromUser(String userId, String roleName) {
        try {
            RealmResource realmResource = keycloak.realm(realm);
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
            realmResource.users().get(userId).roles().realmLevel().remove(Collections.singletonList(role));
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la suppression du rôle", HttpStatus.BAD_REQUEST);
        }
    }

    // Réinitialiser ou changer le mot de passe d'un utilisateur
    public void resetPassword(String userId, String newPassword) {
        try {
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(newPassword);
            keycloak.realm(realm).users().get(userId).resetPassword(passwordCred);
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la réinitialisation du mot de passe", HttpStatus.BAD_REQUEST);
        }
    }

    // Rafraîchir le token d'accès
    public TokenResponse refreshToken(String refreshToken) {
        try {
            String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("refresh_token", refreshToken);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, request, (Class<Map<String, Object>>) (Class<?>) Map.class);
            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("access_token")) {
                throw new ApiException("Token de rafraîchissement invalide", HttpStatus.UNAUTHORIZED);
            }
            return TokenResponse.builder()
                    .token((String) body.get("access_token"))
                    .refreshToken((String) body.get("refresh_token"))
                    .expiresIn(Long.parseLong(body.get("expires_in").toString()))
                    .refreshExpiresIn(Long.parseLong(body.get("refresh_expires_in").toString()))
                    .build();
        } catch (Exception e) {
            throw new ApiException("Token de rafraîchissement invalide", HttpStatus.UNAUTHORIZED);
        }
    }

    // Déconnecter un utilisateur (logout)
    public void logout(String userId) {
        try {
            keycloak.realm(realm).users().get(userId).logout();
        } catch (Exception e) {
            throw new ApiException("Erreur lors de la déconnexion", HttpStatus.BAD_REQUEST);
        }
    }
}
