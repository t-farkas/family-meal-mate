package com.farkas.familymealmate.common.configuration.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title(openApiProperties.getTitle())
                        .version(openApiProperties.getVersion())
                        .description(openApiProperties.getDescription())
                );
    }

}
