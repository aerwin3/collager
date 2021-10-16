package com.collager.images.repository;

import com.collager.images.entity.Image;
import com.collager.images.entity.Tag;
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
    private TestEntityManager entityManager;


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
    public void testThisOut(){
        Image img1 = new Image();
        img1.setAccount("testId");
        img1.setLabel("testLabel1");
        img1.setUrl("img_url1");
        Tag t = new Tag();
        t.setName("book");
        List<Tag> tags = Arrays.asList(t);
        img1.setObjects(tags);
        imageRepository.save(img1);

        entityManager.flush();
        entityManager.clear();
        List<Image> imgs = imageRepository.findImagesByObjects(img1.getAccount(), Arrays.asList("book"));
        assertEquals(1, imgs.size());
    }

}





