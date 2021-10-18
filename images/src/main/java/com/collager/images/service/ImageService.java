package com.collager.images.service;

import com.collager.images.ImagesApplication;
import com.collager.images.entity.Image;
import com.collager.images.repository.ImageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private static final Logger log= LogManager.getLogger(ImagesApplication.class);

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
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
    public Image createImage(String accountId, String label, String url, boolean detection){
        Image img = new Image();
        img.setUrl(url);
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
}
