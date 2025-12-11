package com.farkas.familymealmate.common.configuration.openapi;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:properties/open-api-properties.yaml")
@ConfigurationProperties(prefix = "open-api")
@Component
@Getter
public class OpenApiProperties {

    private String version;
    private String title;
    private String description;

}
