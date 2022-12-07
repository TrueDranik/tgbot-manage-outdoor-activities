package com.bot.sup.service.route.impl;

import com.bot.sup.mapper.RouteMapper;
import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final ScheduleRepository scheduleRepository;

    @Override
    public List<Route> getAllRoute() {
        return routeRepository.findAll();
    }

    @Override
    public Route getRouteById(Long id) {
        return findRouteById(id);
    }

    @Override
    public Route createRoute(RouteCreateDto routeCreateDto) {
        if (routeCreateDto == null) {
            return null;
        }

        Route route = new Route();

        route.setName(routeCreateDto.getName());
        route.setStartPointCoordinates(routeCreateDto.getStartPointCoordinates());
        route.setStartPointName(routeCreateDto.getStartPointName());
        route.setFinishPointCoordinates(routeCreateDto.getFinishPointCoordinates());
        route.setFinishPointName(routeCreateDto.getFinishPointName());
        route.setMapLink(routeCreateDto.getMapLink());
        route.setLength(routeCreateDto.getLength());
        route.setActive(true);

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
        route.setActive(true);

        return routeRepository.save(route);
    }

    @Transactional
    @Override
    public void deleteRoute(Long id) {
        Route routeById = findRouteById(id);
        routeById.setActive(false);

        List<Schedule> schedulesByRouteId = scheduleRepository.findSchedulesByRoute_Id(id);
        for (Schedule schedule :
                schedulesByRouteId) {
            schedule.setActive(false);
        }
    }

    private Route findRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with id[" + id + "] not found"));
    }
}
