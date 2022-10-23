package com.bot.sup.service.route;

import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.entity.Route;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RouteService {
    List<Route> getAllRoute();

    Route getRouteById(Long id);

    Route createRoute(RouteCreateDto createDto);

    Route updateRoute(Long id, RouteCreateDto routeCreateDto);

    void deleteRoute(Long id);
}
