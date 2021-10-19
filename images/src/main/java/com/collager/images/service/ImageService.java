package com.collager.images.service;

import com.collager.images.ImagesApplication;
import com.collager.images.adapter.GCPAdapter;
import com.collager.images.adapter.ImaggaAdapter;
import com.collager.images.adapter.ObjectsAdapter;
import com.collager.images.adapter.StorageAdapter;
import com.collager.images.entity.Image;
import com.collager.images.repository.ImageRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.*;

@Service
public class ImageService {
    private static final Logger log= LogManager.getLogger(ImagesApplication.class);

    private final ImageRepository imageRepository;
    private final StorageAdapter gcpAdapter;
    private final ObjectsAdapter imaggaAdapter;

    public ImageService(ImageRepository imageRepository,
                        GCPAdapter gcpAdapter,
                        ImaggaAdapter imaggaAdapter) {
        this.imageRepository = imageRepository;
        this.gcpAdapter = gcpAdapter;
        this.imaggaAdapter = imaggaAdapter;
    }

    public Image getImage(String account, String Id) {
        return imageRepository.getById(account, UUID.fromString(Id));
    }

    public List<Image> getImagesByAccount(String accountId){
        return imageRepository.findImagesByAccount(accountId);
    }

    public List<Image> getImagesByObjects(String accountId, List<String> objs){
        Set<Image> images = new HashSet<>();
        for (String obj : objs){
            log.info("Find the image: " + obj);
            images.addAll(imageRepository.findImagesByObject(accountId, obj));
        }
        return new ArrayList<>(images);
    }

    public Image createImage(String accountId, String label, String url, MultipartFile file, boolean detection) {
        Image img = new Image();

        // Generate random label
        if (label == null)
            label = DigestUtils.sha256Hex(String.valueOf(Instant.now().toEpochMilli()));

        // Store image
        try{
            img.setLabel(label);
            img.setAccount(accountId);
            img = imageRepository.save(img);
       }catch (Exception e){
           log.error(e);
           return null;
       }

        // Upload file to GCP and store the media link for downloading
        try {
            if (file != null) {
                img.setUrl(uploadFile(img.getId().toString(), file));
            } else {
                img.setUrl(uploadFromUrl(img.getId().toString(), url));
            }
            imageRepository.save(img);
        } catch (IOException e){
            log.error("Error uploading file for storage.", e);
        }
        log.info("Image Created :: " + img);

        // Detect Objects in image
        if (detection) {
            String objs = imaggaAdapter.getObjects(img.getUrl());
            if ( objs != null && !objs.isEmpty()) {
                img.setObjects(objs);
                imageRepository.save(img);
            }
        }
        return img;
    }

    public void removeImage(String id) {
        imageRepository.deleteById(UUID.fromString(id));
    }

    private String uploadFile(String name, MultipartFile f) throws IOException {
        return this.gcpAdapter.upload(name, f.getBytes());
    }

    private String uploadFromUrl(String name, String location) throws IOException {
        URL url = new URL(location);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return this.gcpAdapter.upload(name, output.toByteArray());
    }
}
