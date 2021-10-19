package com.collager.images.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
public class FileStorageProperties {
    private String uploadDir;
    private Integer downloadTimeout;
    private Integer downloadReadTimeout;

    public String getUploadDir() {
        return uploadDir;
    }

    public Integer getDownloadTimeout() {
        return downloadTimeout;
    }

    public Integer getDownloadReadTimeout() {
        return downloadReadTimeout;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void setDownloadTimeout(Integer downloadTimeout) {
        this.downloadTimeout = downloadTimeout;
    }

    public void setDownloadReadTimeout(Integer downloadReadTimeout) {
        this.downloadTimeout = downloadReadTimeout;
    }
}