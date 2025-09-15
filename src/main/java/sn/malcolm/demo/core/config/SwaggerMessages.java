package sn.malcolm.demo.core.config;


import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerMessages {

    @Bean
    public OpenApiCustomizer globalApiCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                ApiResponses responses = operation.getResponses();

                // Ajouter des réponses standard si elles n'existent pas déjà
                if (responses.get("400") == null) {
                    responses.addApiResponse("400", new ApiResponse().description("Requête incorrecte"));
                }
                if (responses.get("401") == null) {
                    responses.addApiResponse("401", new ApiResponse().description("Non autorisé - Authentification requise"));
                }
                if (responses.get("403") == null) {
                    responses.addApiResponse("403", new ApiResponse().description("Accès interdit - Droits insuffisants"));
                }
                if (responses.get("404") == null) {
                    responses.addApiResponse("404", new ApiResponse().description("Ressource non trouvée"));
                }
                if (responses.get("500") == null) {
                    responses.addApiResponse("500", new ApiResponse().description("Erreur interne du serveur"));
                }
            }));
        };
    }
}