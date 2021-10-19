package com.collager.images.service;

import com.collager.images.entity.Image;
import com.collager.images.property.FileStorageProperties;
import com.collager.images.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@EnableConfigurationProperties({FileStorageProperties.class})
@TestPropertySource("/test-application.properties")
public class ImageServiceTests {

    private ImageService imageService;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    public void initTest(){
        imageService = new ImageService(imageRepository, fileStorageProperties);
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

    @Test
    public void deleteImageTest(){
        UUID uuid = UUID.randomUUID();
        imageService.removeImage("acct", uuid.toString());
        verify(imageRepository, Mockito.times(1)).deleteById(uuid);
    }

    @Test
    public void createImageMultipartFile() throws IOException {
        Image img = new Image();
        when(imageRepository.save(any())).thenReturn(img);
        imageService.createImage("a","label", "", multipartFile, true);
        verify(multipartFile, Mockito.times(1)).transferTo(any(File.class));
    }
}
