package com.bot.sup.repository;

import com.bot.sup.model.entity.AboutUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {

    @Query("SELECT fullDescription FROM AboutUs")
    String getFullDescription();
}
