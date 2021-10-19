package com.collager.images.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "file")
@Component
public class FileStorageProperties {
    private String uploadDir;
    private Integer downloadTimeout;
    private Integer downloadReadTimeout;
}