package com.bot.sup.repository;

import com.bot.sup.model.entity.AboutUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {

    @Query("SELECT au FROM AboutUs au")
    Optional<AboutUs> getAboutUs();
}
