package com.collager.images.adapter;

import com.collager.images.property.GCPStorageProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.Storage.PredefinedAcl;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Getter
@Setter
@Component
public class GCPAdapter implements StorageAdapter{

    Storage storage;
    GCPStorageProperties properties;

    public GCPAdapter(GCPStorageProperties gcpStorageProperties) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(gcpStorageProperties.getCredentials()))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        this.storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        this.properties = gcpStorageProperties;

        Bucket bucket = storage.get(gcpStorageProperties.getBucketName(),
                Storage.BucketGetOption.fields(Storage.BucketField.values()));
        if (!bucket.exists()){
            storage.create(BucketInfo.of(gcpStorageProperties.getBucketName()));
        }
    }

    public String upload(String name, byte[] data) {
        try {
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(this.properties.getBucketName(), name).build(),
                    data,
                    BlobTargetOption.predefinedAcl(PredefinedAcl.PUBLIC_READ)
            );
            return blobInfo.getMediaLink(); // Return file url
        }catch(IllegalStateException e){
            throw new RuntimeException(e);
        }
    }
}