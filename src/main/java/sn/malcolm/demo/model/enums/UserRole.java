package sn.malcolm.demo.model.enums;

/**
 * Enumération des rôles disponibles dans l'application.
 * Ces rôles doivent correspondre aux rôles configurés dans Keycloak.
 */
public enum UserRole {
    ADMIN("admin"),
    USER("user");
    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getValue() {
        return roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

}
