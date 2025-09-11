package sn.malcolm.demo.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sn.malcolm.demo.model.User;
import sn.malcolm.demo.repository.UserRepository;
import sn.malcolm.demo.security.UserSec;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        // Verifier si l'authentification existe et si le principal est un JWT
        if (existingAuth != null && existingAuth.getPrincipal() instanceof Jwt) {
            try {
                Jwt jwt = (Jwt) existingAuth.getPrincipal();

                // Extraction de l'ID utilisateur depuis le JWT
                String userId = jwt.getSubject();

                // Vérifier si le claim "realm_access" existe
                Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

                log.info("User ID from JWT: {}", userId);
                // Creation des autorités à partir des rôles dans le claim "realm_access"
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    @SuppressWarnings("unchecked")
                    List<String> roles = (List<String>) realmAccess.get("roles");
                    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                }

                log.info("Authorities from JWT: {}", authorities);


                // Rechercher l'utilisateur dans la base de données
                Optional<User> userOpt = userRepository.findByKcId(userId);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    log.info("User found in database: {}", user.getUsername());

                    // Creation de l'objet UserSec
                    UserSec userSec = new UserSec();
                    userSec.setId(user.getKcId());
                    userSec.setUserId(user.getId());
                    userSec.setUsername(user.getUsername());
                    userSec.setEmail(user.getEmail());
                    userSec.setContactName(user.getFirstName());
                    userSec.setActive(user.isActivate());
                    userSec.setIsBoUser(user.getIsBoUser());
                    userSec.setAuthorities(authorities);

                    log.info(
                            "UserSec created: ID: {}, Login: {}, Email: {}, Username: {}",
                            userSec.getId(), userSec.getUsername(), userSec.getEmail(),
                            userSec.getContactName()
                    );

                    Authentication newAuth = new UsernamePasswordAuthenticationToken(
                            userSec, null, authorities);
                    log.info("New authentication created: {}", newAuth);
                    SecurityContextHolder.getContext().setAuthentication(newAuth);

                    log.info("User {} authenticated successfully and UserSec created", user.getUsername());
                } else {
                    log.error("User with KC ID {} not found in our database", userId);
                }
            } catch (Exception e) {
                log.error("Error processing JWT authentication", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}