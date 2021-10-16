package com.collager.images.service;

import com.collager.images.entity.Image;
import com.collager.images.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTests {

    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @BeforeEach
    public void initTest(){
        imageService = new ImageService(imageRepository);
    }

    @Test
    public void getImageTest(){
        UUID uuid = UUID.randomUUID();
        when(imageRepository.getById("acct", uuid)).thenReturn(new Image(uuid,"acct","label", "url", null));
        assertEquals(uuid, imageService.getImage("acct", uuid.toString()).getId());
    }

    @Test
    public void getImageNotFoundTest(){
        UUID uuid = UUID.randomUUID();
        when(imageRepository.getById(uuid)).thenReturn(null);
        assertNull(imageService.getImage("acct", uuid.toString()));
    }



}
