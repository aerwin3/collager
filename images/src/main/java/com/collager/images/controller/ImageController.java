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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ImageController {

    private ImageService imageService;

    private static final Logger log = LogManager.getLogger(ImagesApplication.class);

    public ImageController(ImageService imageService) {
        super();
        this.imageService = imageService;
    }

    @GetMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    HashMap<String, Object> listImages(
            @RequestHeader("X-Account-Id") String account,
            @RequestParam HashMap<String, Object> params) {
        HashMap<String, Object> imageResponse = new HashMap<>();
        Set<Image> images = new HashSet<>();
        if (params.get("id") != null) {
            images.add(imageService.getImage(account, params.get("id").toString()));
        } else if (params.get("objects") != null) {
            images.addAll(imageService.getImagesByObjects(account, Arrays.asList(params.get("objects").toString().split(","))));
        } else {
            images.addAll(imageService.getImagesByAccount(account));
        }
        imageResponse.put("items", images);
        return imageResponse;
    }

    @PostMapping(value = "/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Image createImage(
            @RequestHeader("X-Account-Id") String account,
            @RequestBody ImageRequest image) {
        return imageService.createImage(account, image.label, image.url, true);
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
