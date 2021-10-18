package com.collager.images.repository;

import com.collager.images.entity.Image;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest()
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.properties")
public class ImageRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ImageRepository imageRepository;

    @Test
    public void testImageRepositoryGetByAccountId(){
        Image img1 = new Image();
        img1.setAccount("testId");
        img1.setLabel("testLabel1");
        img1.setUrl("img_url1");
        Image img2 = new Image();
        img2.setAccount("testId");
        img2.setLabel("testLabel2");
        img2.setUrl("img_url2");
        imageRepository.save(img1);
        imageRepository.save(img2);
        List<Image> images = imageRepository.findImagesByAccount("testId");
        assertEquals(2, images.size());
    }

    @Test
    public void testGetImageById(){
        Image img1 = new Image();
        img1.setAccount("testId");
        img1.setLabel("testLabel1");
        img1.setUrl("img_url1");
        imageRepository.saveAndFlush(img1);
        Image image = imageRepository.getById(img1.getId());
        assertEquals(img1.getId(), image.getId());
    }

    @Test
    public void testFindImagesByObject(){
        for (int i=0; i<2; i++){
            Image img = new Image();
            img.setAccount("a");
            img.setLabel("testLabel"+i);
            img.setUrl("img_url"+i);
            img.setObjects("book,apple");
            imageRepository.save(img);
        }


        List<Image> imgs = imageRepository.findImagesByObject("a", "book");
        assertEquals(2, imgs.size());
    }

}





