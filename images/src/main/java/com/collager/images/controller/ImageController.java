package com.collager.images.controller;

import com.collager.images.entity.Image;
import com.collager.images.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService){
        super();
        this.imageService=imageService;
    }

    @GetMapping(value="/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HashMap<String, Object> listImages(
            @RequestHeader("X-Account-Id") String account,
            @RequestParam HashMap<String, Object> params) {
        HashMap<String, Object> imageResponse = new HashMap<>();
        List<Image> images= new ArrayList<>();
        if (params.get("id") != null){
            images.add(imageService.getImage(account, params.get("id").toString()));
        } else {
            images.addAll(imageService.getImagesByAccount(account));
        }
        imageResponse.put("items", images);
        return imageResponse;
    }

    @PostMapping(value="/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Image createImage(
            @RequestHeader("X-Account-Id") String account,
            @RequestBody ImageRequest image){
        return imageService.createImage(account, image.label, image.url, image.detection);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ImageRequest {
        String label;
        String url;
        Boolean detection;
    }
}
