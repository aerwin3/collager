package com.collager.images.controller;

import com.collager.images.ImagesApplication;
import com.collager.images.entity.Image;
import com.collager.images.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ImageController {

    private ImageService imageService;

    private static final Logger log = LogManager.getLogger(ImagesApplication.class);

    public ImageController(ImageService imageService) {
        super();
        this.imageService = imageService;
    }

    @GetMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listImages(
            @RequestHeader("X-Account-Id") String account,
            @RequestParam HashMap<String, Object> params) {
        HashMap<String, Object> imageResponse = new HashMap<>();
        Set<Image> images = new HashSet<>();
        if (params.get("objects") != null) {
            List<String> objs = Arrays.asList(params.get("objects").toString().split(","));
            images.addAll(imageService.getImagesByObjects(account, objs));
        } else {
            images.addAll(imageService.getImagesByAccount(account));
        }
        imageResponse.put("items", images);
        return ResponseEntity.ok(imageResponse);
    }

    @PostMapping(value = "/images")
    public ResponseEntity<?> createImage(
            @RequestHeader("X-Account-Id") String account,
            @RequestBody ImageRequest image) {
        Image img = imageService.createImage(account, image.label, image.url, true);
        if (img == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(img);
    }

    @GetMapping(value = "/images/{id}")
    public ResponseEntity<?> getImage(
            @RequestHeader("X-Account-Id") String account,
            @PathVariable String id) {
        Image img = imageService.getImage(account, id);
        if (img == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(img);
    }

    @DeleteMapping(value = "/images/{id}")
    public ResponseEntity<?> deleteImage(
            @RequestHeader(value = "X-Account-Id") String account,
            @PathVariable String id) {
        imageService.removeImage(account, id);
        return ResponseEntity.noContent().build();
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    private static class ImageRequest {
        String label;
        String url;
        Boolean detection;
    }
}
