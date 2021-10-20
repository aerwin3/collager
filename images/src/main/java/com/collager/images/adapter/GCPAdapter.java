package com.collager.images.adapter;

import com.collager.images.property.GCPStorageProperties;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.Storage.PredefinedAcl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GCPAdapter implements StorageAdapter{

    Storage storage;
    GCPStorageProperties properties;

    public GCPAdapter(GCPStorageProperties gcpStorageProperties) {
        this.storage = StorageOptions.getDefaultInstance().getService();
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

    @Override
    public boolean remove(String id) {
        return storage.delete(properties.getBucketName(), id);
    }

}