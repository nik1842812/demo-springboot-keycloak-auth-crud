package sn.malcolm.demo.security.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import sn.malcolm.demo.security.UserSec;

public class AuthenticationSystem {

    public static boolean isLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }


    public static UserSec getCurrentUser() {
        if (isLogged()) {
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserSec) authentication.getPrincipal();
    }

    public static boolean isAdmin() {
        if (isLogged()) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_admin"));
    }

    public static boolean isBoUser() {
        UserSec userSec = getCurrentUser();
        return userSec != null && userSec.getIsBoUser();
    }
}