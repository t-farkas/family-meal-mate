package com.farkas.familymealmate.common.configuration.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_FORMAT = "JWT";
    private static final String BEARER = "bearer";

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(openApiProperties.getTitle())
                        .version(openApiProperties.getVersion())
                        .description(openApiProperties.getDescription())
                )
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(AUTH_HEADER)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme(BEARER)
                                        .bearerFormat(BEARER_FORMAT)
                                        .in(SecurityScheme.In.HEADER)
                        ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

}
