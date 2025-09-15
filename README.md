# 🚀 Demo Spring Boot + Keycloak

## 📝 Présentation

Ce projet est un exemple d’application Spring Boot intégrant Keycloak pour la gestion de l’authentification, l’inscription des utilisateurs et les opérations CRUD. Il s’adresse à toute personne souhaitant sécuriser une API REST avec Keycloak.

## ✨ Fonctionnalités

- 🔐 Authentification via Keycloak
- 📝 Inscription d’utilisateurs
- 🛡️ Gestion des rôles (admin, user)
- 🗂️ Opérations CRUD sur les entités principales

## ⚙️ Prérequis

- ☕ Java 17+
- 🐘 Maven
- 🐳 Docker (pour Keycloak)

## 🚦 Installation et démarrage

1. **Cloner le projet**

   ```bash
   git clone https://github.com/bayembacke221/demo-springboot-keycloak-auth-crud.git
   cd demo-springboot-keycloak-auth-crud
   ```

2. **Lancer Keycloak avec Docker Compose**

   ```bash
   docker-compose up -d
   ```

3. **Configurer Keycloak**

   Suivre les étapes ci-dessous (voir section "Configuration Keycloak").

4. **Lancer l’application Spring Boot**

   ```bash
   ./mvnw spring-boot:run
   ```

## 🛠️ Configuration Keycloak

1. **Créer un Realm**  
<img src="keycloak/create_realm.png" alt="Créer un Realm" width="600"/>

2. **Créer un Client**  
<img src="keycloak/1-create-client-id General settings.png" alt="Client - Paramètres généraux" width="600"/>
<br>
<img src="keycloak/2-create-client-id Capability config.png" alt="Client - Capabilities" width="600"/>
<br>
<img src="keycloak/3-create-client-id Login settings.png" alt="Client - Login settings" width="600"/>

3. **Créer des rôles**  
<img src="keycloak/creation_role.png" alt="Création d’un rôle" width="600"/>
<br>
<img src="keycloak/admin_role.png" alt="Rôle admin" width="600"/>
<br>
<img src="keycloak/user_role.png" alt="Rôle user" width="600"/>

4. **Créer un utilisateur**  
<img src="keycloak/create_user.png" alt="Créer un utilisateur" width="600"/>
<br>
<img src="keycloak/set-password.png" alt="Définir le mot de passe" width="600"/>
<br>
<img src="keycloak/assign-role-user.png" alt="Ajouter un rôle à l’utilisateur" width="600"/>

## 📡 Utilisation de l’API

- 🔑 **Authentification** : Obtenir un token via `/auth/realms/demo/protocol/openid-connect/token`
- 📝 **Inscription** : Endpoint d’inscription utilisateur (exemple : `/api/register`)
- 🗂️ **CRUD** : Endpoints pour créer, lire, mettre à jour, supprimer des entités protégées par Keycloak

> Voir le code source pour le détail des endpoints (`src/main/java/sn/malcolm/demo/controller/`)

## 🗃️ Structure du projet

```text
src/
  main/
    java/sn/malcolm/demo/
      controller/      # Contrôleurs REST
      model/           # Entités JPA
      repository/      # Repositories Spring Data
      security/        # Configurations Keycloak
      service/         # Logique métier
      view/            # DTOs
  resources/
    application.properties
keycloak/              # Captures d’écran de la configuration Keycloak
```

## 🖼️ Captures d’écran de l’application

Voici quelques exemples d’utilisation de l’API via Postman :

### 🔑 Authentification (Login)
<img src="postman/login_and_generate_token_for_inherith_auth_from_parent.png" alt="Login et génération du token" width="500"/>

### 📝 Création d’un utilisateur
<img src="postman/createUser.png" alt="Création d'utilisateur" width="500"/>

### 📋 Récupérer tous les utilisateurs
<img src="postman/getAllUser.png" alt="Get All Users" width="500"/>

### 🔍 Récupérer un utilisateur par ID
<img src="postman/getUserByID.png" alt="Get User By ID" width="500"/>

### ✏️ Mise à jour d’un utilisateur
<img src="postman/updateUser.png" alt="Update User" width="500"/>

### ❌ Suppression d’un utilisateur
<img src="postman/deleteUser.png" alt="Delete User" width="500"/>

## 📚 Ressources utiles

- [📖 Documentation Keycloak](https://www.keycloak.org/documentation)
- [🔗 Spring Security & Keycloak](https://www.baeldung.com/spring-boot-keycloak)

## 👤 Auteur

- Mbacke Mbaye

---
N’hésite pas à contribuer ou à ouvrir une issue ! 😃
