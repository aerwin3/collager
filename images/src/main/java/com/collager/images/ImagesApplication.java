package com.collager.images;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EnableJpaRepositories("com.collager.images.repository")
@EntityScan("com.collager.images.entity")
@SpringBootApplication
public class ImagesApplication {
    private static final Logger log = LogManager.getLogger(ImagesApplication.class);

    public static void main(String[] args) {
        log.info("Starting Up Collager Service");
        SpringApplication.run(ImagesApplication.class, args);
    }

}
