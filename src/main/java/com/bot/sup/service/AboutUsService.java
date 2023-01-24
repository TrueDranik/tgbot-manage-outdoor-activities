package com.bot.sup.service;

import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.repository.AboutUsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsService {
    private final AboutUsRepository aboutUsRepository;

    public void save(AboutUs aboutUs){
        aboutUsRepository.save(aboutUs);
    }

    public Optional<AboutUs> findById(Long aboutUsId) {
        return aboutUsRepository.findById(aboutUsId);
    }
}
