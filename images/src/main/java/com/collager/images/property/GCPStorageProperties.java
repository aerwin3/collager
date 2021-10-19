package com.collager.images.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "gcp")
@Component
public class GCPStorageProperties {
    private String bucketName;
    private String credentials;
}
