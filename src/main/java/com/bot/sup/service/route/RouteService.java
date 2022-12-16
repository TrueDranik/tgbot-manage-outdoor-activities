package com.bot.sup.service.route;

import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.dto.RouteDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RouteService {
    List<RouteDto> getAllRoute();

    RouteDto getRouteById(Long id);

    RouteDto createRoute(RouteCreateDto createDto);

    RouteDto updateRoute(Long id, RouteCreateDto routeCreateDto);

    void deleteRoute(Long id);
}
