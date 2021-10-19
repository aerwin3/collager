package com.collager.images;

import com.collager.images.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableJpaRepositories("com.collager.images.repository")
@EntityScan("com.collager.images.entity")
@SpringBootApplication
public class ImagesApplication {
    private static final Logger LOGGER=LogManager.getLogger(ImagesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ImagesApplication.class, args);
    }

}
