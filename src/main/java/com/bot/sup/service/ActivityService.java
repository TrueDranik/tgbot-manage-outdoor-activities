package com.bot.sup.service;

import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final RouteRepository routeRepository;

    public void save(Route route) {
        routeRepository.save(route);
    }
}
