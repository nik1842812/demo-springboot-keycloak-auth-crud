# 🚀 Demo Spring Boot + Keycloak

## 📝 Overview

This project is a sample Spring Boot application integrating Keycloak for authentication, user registration, sending email, and CRUD operations. It is intended for anyone looking to secure a REST API with Keycloak.

## ✨ Features

- 🔐 Authentication via Keycloak
- 📝 User registration
- 🛡️ Role management (admin, user)
- 🗂️ CRUD operations on main entities

## ⚙️ Prerequisites

- ☕ Java 17+
- 🐘 Maven
- 🐳 Docker (for Keycloak)

## 🚦 Installation & Startup

1. **Clone the project**

   ```bash
   git clone https://github.com/bayembacke221/demo-springboot-keycloak-auth-crud.git
   cd demo-springboot-keycloak-auth-crud
   ```

2. **Start Keycloak with Docker Compose**

   ```bash
   docker-compose up -d
   ```

3. **Configure Keycloak**

   Follow the steps below (see "Keycloak Configuration" section).

4. **Start the Spring Boot application**

   ```bash
   ./mvnw spring-boot:run
   ```

## 🛠️ Keycloak Configuration

1. **Create a Realm**  
<img src="keycloak/create_realm.png" alt="Create Realm" width="600"/>

2. **Create a Client**  
<img src="keycloak/1-create-client-id General settings.png" alt="Client - General settings" width="600"/>
<br>
<img src="keycloak/2-create-client-id Capability config.png" alt="Client - Capabilities" width="600"/>
<br>
<img src="keycloak/3-create-client-id Login settings.png" alt="Client - Login settings" width="600"/>

3. **Create roles**  
<img src="keycloak/creation_role.png" alt="Create role" width="600"/>
<br>
<img src="keycloak/admin_role.png" alt="Admin role" width="600"/>
<br>
<img src="keycloak/user_role.png" alt="User role" width="600"/>

4. **Create a user**  
<img src="keycloak/create_user.png" alt="Create user" width="600"/>
<br>
<img src="keycloak/set-password.png" alt="Set password" width="600"/>
<br>
<img src="keycloak/assign-role-user.png" alt="Assign role to user" width="600"/>

## 📡 API Usage

- 🔑 **Authentication**: Get a token via `/auth/realms/demo/protocol/openid-connect/token`
- 📝 **Registration**: User registration endpoint (e.g. `/api/register`)
- 🗂️ **CRUD**: Endpoints to create, read, update, and delete entities protected by Keycloak

> See the source code for endpoint details (`src/main/java/sn/malcolm/demo/controller/`)

## 🗃️ Project Structure

```text
src/
  main/
    java/sn/malcolm/demo/
      controller/      # REST Controllers
      model/           # JPA Entities
      repository/      # Spring Data Repositories
      security/        # Keycloak Configurations
      service/         # Business Logic
      view/            # DTOs
  resources/
    application.properties
keycloak/              # Keycloak configuration screenshots
```

## 🖼️ Application Screenshots

Here are some examples of API usage with Postman:

### 🔑 Authentication (Login)
<img src="postman/login_and_generate_token_for_inherith_auth_from_parent.png" alt="Login and generate token" width="500"/>

### 📝 Create a user
<img src="postman/createUser.png" alt="Create user" width="500"/>

### 📧 Send email creation
<img src="postman/email-creation-account.png" alt="Send Email" width="500"/>

### 📋 Get all users
<img src="postman/getAllUser.png" alt="Get All Users" width="500"/>

### 🔍 Get user by ID
<img src="postman/getUserByID.png" alt="Get User By ID" width="500"/>

### ✏️ Update a user
<img src="postman/updateUser.png" alt="Update User" width="500"/>

### ❌ Delete a user
<img src="postman/deleteUser.png" alt="Delete User" width="500"/>

### 🔄 Reset password (reset-password)

<img src="postman/reset-password.png" alt="Reset Password" width="500"/>
<img src="postman/mail-reset-password.png" alt="Mail Reset Password" width="500"/>

### 🔒 Change password (change-password)

<img src="postman/change-password.png" alt="Change Password" width="500"/>

## 📚 Useful Resources

- [📖 Keycloak Documentation](https://www.keycloak.org/documentation)
- [🔗 Spring Security & Keycloak](https://www.baeldung.com/spring-boot-keycloak)

## 👤 Author

- Mbacke Mbaye

---
Feel free to contribute or open an issue! 😃
