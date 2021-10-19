package com.collager.images.service;

import com.collager.images.ImagesApplication;
import com.collager.images.adapter.GCPAdapter;
import com.collager.images.entity.Image;
import com.collager.images.property.FileStorageProperties;
import com.collager.images.property.GCPStorageProperties;
import com.collager.images.repository.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
public class ImageService {
    private static final Logger log= LogManager.getLogger(ImagesApplication.class);

    private final ImageRepository imageRepository;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    private GCPAdapter gcpAdapter;

    public ImageService(ImageRepository imageRepository,
                        FileStorageProperties fileStorageProperties) {
        this.imageRepository = imageRepository;
        this.fileStorageProperties = fileStorageProperties;
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

    public Image createImage(String accountId, String label, String url, MultipartFile file, boolean detection) throws IOException {
        Image img = new Image();
        try{
            img.setLabel(label);
            img.setAccount(accountId);
            img = imageRepository.save(img);
            log.info("Image Created :: " + img);
       }catch (Exception e){
           log.error(e);
           return null;
       }
        if (file != null){
            img.setUrl( uploadFile(img.getId().toString(), file));
        } else {
            img.setUrl(uploadFromUrl(img.getId().toString(), url));
        }
        imageRepository.save(img);

        return img;
    }

    public void removeImage(String acct, String id) {
        imageRepository.deleteById(UUID.fromString(id));
        log.info("Image " + id +" removed from account " + acct);
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
