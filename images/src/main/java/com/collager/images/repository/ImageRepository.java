package com.collager.images.repository;

import com.collager.images.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID>{

    @Query("select img from Image img where img.account=(:account)")
    List<Image> findImagesByAccount(@Param("account") String account);

    @Query("select img from Image img where img.account=(:account) and img.id=(:uuid)")
    Image getById(@Param("account") String account, UUID uuid);

    @Query("select img from Image img join img.objects o where img.account=(:account) and o.name in (:names)")
    List<Image> findImagesByObjects(@Param("account") String account, @Param("names") List<String> names );

}
