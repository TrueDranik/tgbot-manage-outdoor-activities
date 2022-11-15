package com.bot.sup.service.route.impl;

import com.bot.sup.mapper.RouteMapper;
import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Override
    public List<Route> getAllRoute() {
        List<Route> routes = new ArrayList<>(routeRepository.findAll());

        if (routes.isEmpty()){
            throw new EntityNotFoundException("Routes not found");
        }

        return routes;
    }

    @Override
    public Route getRouteById(Long id) {
        return findRouteById(id);
    }

    @Override
    public Route createRoute(RouteCreateDto createDto) {
        Route route = routeMapper.dtoToDomain(createDto);

        return routeRepository.save(route);
    }

    @Override
    public Route updateRoute(Long id, RouteCreateDto routeCreateDto) {
        Route route = findRouteById(id);

        route.setName(routeCreateDto.getName());
        route.setStartPointCoordinates(routeCreateDto.getStartPointCoordinates());
        route.setStartPointName(routeCreateDto.getStartPointName());
        route.setFinishPointCoordinates(routeCreateDto.getFinishPointCoordinates());
        route.setFinishPointName(routeCreateDto.getFinishPointName());
        route.setMapLink(routeCreateDto.getMapLink());
        route.setLength(routeCreateDto.getLength());

        return routeRepository.save(route);
    }

    @Override
    public void deleteRoute(Long id) {
        routeRepository.delete(findRouteById(id));
    }

    private Route findRouteById(Long id){
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with id[" + id + "] not found"));
    }
}
