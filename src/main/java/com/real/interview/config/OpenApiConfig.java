package com.real.interview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String oauthSchemeName = "oauth2";
        return new OpenAPI()
            .info(new Info().title("API").version("v1"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .addSecurityItem(new SecurityRequirement().addList(oauthSchemeName))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecuritySchemes(oauthSchemeName,
                    new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(new io.swagger.v3.oas.models.security.OAuthFlows()
                            .authorizationCode(new io.swagger.v3.oas.models.security.OAuthFlow()
                                .authorizationUrl("/oauth2/authorization/google")
                                .tokenUrl("/oauth2/token")
                            )
                        )
                )
            );
    }
}

