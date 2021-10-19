package com.collager.images.repository;

import com.collager.images.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID>{

    @Query("select img from Image img where img.account=(:account)")
    List<Image> findImagesByAccount(@Param("account") String account);

    @Query("select img from Image img where img.account=(:account) and img.id=(:uuid)")
    Image getById(@Param("account") String account, UUID uuid);

    @Query("select img from Image img where img.account=(:account) and img.objects like %:name%")
    List<Image> findImagesByObject(@Param("account") String account, @Param("name") String name );

}
