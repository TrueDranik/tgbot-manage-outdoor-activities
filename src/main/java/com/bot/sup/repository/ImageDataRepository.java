package com.bot.sup.repository;

import com.bot.sup.model.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String name);

    @Query("SELECT i FROM ImageData i INNER JOIN Route r ON i.id = r.imageData.id WHERE r.id = ?1")
    Optional<ImageData> findByRouteId(Long id);
}
