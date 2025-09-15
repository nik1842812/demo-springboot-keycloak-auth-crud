package sn.malcolm.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.malcolm.demo.core.exception.ApiException;
import sn.malcolm.demo.core.helper.AppUtil;
import sn.malcolm.demo.repository.UserRepository;
import sn.malcolm.demo.service.KeycloakService;
import sn.malcolm.demo.model.User;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    public static final Integer DFT_PWD_LENGTH = 12;

    public UserService(UserRepository userRepository, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
    }

    // Créer un utilisateur (Keycloak + base de données)
    public User createUser(User user) {
        String password = AppUtil.generateRandomPassword(DFT_PWD_LENGTH);
        // 1. Créer l'utilisateur dans Keycloak
        String kcId = keycloakService.createUser(user, password);
        user.setKcId(kcId);
        // 2. Sauvegarder l'utilisateur en base
        return userRepository.save(user);
    }

    // Mettre à jour un utilisateur
    public User updateUser(Integer userId, User userUpdate) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ApiException("Utilisateur non trouvé"));
        // Mettre à jour dans Keycloak
        keycloakService.updateUser(user.getKcId(), userUpdate);
        // Mettre à jour en base
        user.setEmail(userUpdate.getEmail());
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        return userRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ApiException("Utilisateur non trouvé"));
        keycloakService.deleteUser(user.getKcId());
        userRepository.delete(user);
    }

    // Récupérer un utilisateur par ID
    public User getUserById(Integer userId) {
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ApiException("Utilisateur non trouvé"));
    }

    // Récupérer un utilisateur par username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Utilisateur non trouvé"));
    }

    // Lister tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
