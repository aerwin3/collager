package com.collager.images.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "imagga")
@Component
public class ImaggaProperties {
    private String apiEndpoint;
    private String key;
    private String secret;
}
