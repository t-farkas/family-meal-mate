package com.farkas.familymealmate.common.configuration.openapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "open-api")
@Component
@Getter
@Setter
public class OpenApiProperties {

    private String version;
    private String title;
    private String description;

}
