package com.bot.sup.service.route.impl;

import com.bot.sup.mapper.RouteMapper;
import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Override
    public List<Route> getAllRoute() {
        return null;
    }

    @Override
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Route createRoute(RouteCreateDto createDto) {
        Route route = routeMapper.dtoToDomain(createDto);

        return routeRepository.save(route);
    }

    @Override
    public Route updateRoute(Long id, Route activity) {
        return null;
    }

    @Override
    public void deleteRoute(Long id) {

    }
}
