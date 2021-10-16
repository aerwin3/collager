package com.collager.images.service;

import com.collager.images.ImagesApplication;
import com.collager.images.entity.Image;
import com.collager.images.repository.ImageRepository;
import org.apache.el.parser.AstFalse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public Image createImage(String accountId, String label, String url, boolean detection){
        Image img = new Image();
        img.setUrl(url);
        img.setLabel(label);
        img.setAccount(accountId);
        img = imageRepository.save(img);
        log.info("Image Created :: " + img);
        return img;
    }
}
