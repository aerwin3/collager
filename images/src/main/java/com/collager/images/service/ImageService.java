package com.collager.images.service;

import com.collager.images.ImagesApplication;
import com.collager.images.entity.Image;
import com.collager.images.property.FileStorageProperties;
import com.collager.images.repository.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class ImageService {
    private static final Logger log= LogManager.getLogger(ImagesApplication.class);

    private final ImageRepository imageRepository;
    private final FileStorageProperties fileStorageProperties;

    public ImageService(ImageRepository imageRepository, FileStorageProperties fileStorageProperties) {
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

        if (file != null){
            img.setUrl(uploadFile(file));
        } else {
            img.setUrl(uploadFromUrl(url));
        }
        // TODO: Generate Label if needed
        img.setLabel(label);
        img.setAccount(accountId);
        try{
            img = imageRepository.save(img);
            log.info("Image Created :: " + img);
       }catch (Exception e){
           log.error(e);
           return null;
       }
        return img;
    }

    public void removeImage(String acct, String id) {
        imageRepository.deleteById(UUID.fromString(id));
        log.info("Image " + id +" removed from account " + acct);
    }

    private String uploadFile(MultipartFile f) throws IOException {
        String dest = this.fileStorageProperties.getUploadDir() + f.getOriginalFilename();
        f.transferTo(new File(dest));
        return dest;
    }

    private String uploadFromUrl(String url) throws IOException {
        String dest = this.fileStorageProperties.getUploadDir() + "tmpfile";
        FileUtils.copyURLToFile(
                new URL(url),
                new File(dest),
                this.fileStorageProperties.getDownloadTimeout(),
                this.fileStorageProperties.getDownloadReadTimeout());
        return dest;
    }
}
