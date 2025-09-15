package sn.malcolm.demo.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${keycloak.auth-server-url:http://localhost:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:Demo}")
    private String realm;

    private static final String OAUTH_SCHEME_NAME = "keycloak";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("demo-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        // Construction de l'URL du token de Keycloak
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .password(new OAuthFlow()
                                                .tokenUrl(tokenUrl)
                                        )
                                )
                        ))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
                .info(new Info()
                        .title("Demo API")
                        .version("1.0")
                        .description("Documentation de l'API Demo")
                        .contact(new Contact()
                                .name("Demo")
                                .email("contact@mbacke.sn")
                                .url("https://test.sn")
                        )
                );
    }
}