package com.bot.sup.service.route.impl;

import com.bot.sup.mapper.RouteMapper;
import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.dto.RouteDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ImageDataRepository;
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
    private final ScheduleRepository scheduleRepository;
    private final ImageDataRepository imageDataRepository;
    private final RouteMapper routeMapper;

    @Override
    public List<RouteDto> getAllRoute() {
        List<Route> routes = routeRepository.findAll();

        return routeMapper.routesToDtos(routes);
    }

    @Override
    public RouteDto getRouteById(Long id) {
        Route routeById = findRouteById(id);

        return routeToDto(routeById);
    }

    @Override
    public RouteDto createRoute(RouteCreateDto routeCreateDto) {
        if (routeCreateDto == null) {
            return null;
        }

        Route route = new Route();

        dtoToRoute(routeCreateDto, route);

        routeRepository.save(route);

        return routeToDto(route);
    }

    @Override
    public RouteDto updateRoute(Long id, RouteCreateDto routeCreateDto) {
        Route route = findRouteById(id);

        dtoToRoute(routeCreateDto, route);

        routeRepository.save(route);

        return routeToDto(route);
    }

    @Transactional
    @Override
    public void deleteRoute(Long id) {
        Route routeById = findRouteById(id);
        routeById.setIsActive(false);

        List<Schedule> schedulesByRouteId = scheduleRepository.findSchedulesByRoute_Id(id);
        for (Schedule schedule : schedulesByRouteId) {
            schedule.setIsActive(false);
        }

        routeRepository.save(routeById);
        scheduleRepository.saveAll(schedulesByRouteId);
    }

    private Route findRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with id[" + id + "] not found"));
    }

    private static RouteDto routeToDto(Route route) {
        RouteDto routeDto = new RouteDto();

        routeDto.setId(route.getId());
        routeDto.setName(route.getName());
        routeDto.setStartPointCoordinates(route.getStartPointCoordinates());
        routeDto.setStartPointName(route.getStartPointName());
        routeDto.setFinishPointCoordinates(route.getFinishPointCoordinates());
        routeDto.setFinishPointName(route.getFinishPointName());
        routeDto.setMapLink(route.getMapLink());
        routeDto.setLength(route.getLength());
        routeDto.setIsActive(routeDto.getIsActive());

        return routeDto;
    }

    private void dtoToRoute(RouteCreateDto routeCreateDto, Route route) {
        route.setName(routeCreateDto.getName());
        route.setStartPointCoordinates(routeCreateDto.getStartPointCoordinates());
        route.setStartPointName(routeCreateDto.getStartPointName());
        route.setFinishPointCoordinates(routeCreateDto.getFinishPointCoordinates());
        route.setFinishPointName(routeCreateDto.getFinishPointName());
        route.setMapLink(routeCreateDto.getMapLink());
        route.setLength(routeCreateDto.getLength());
        route.setIsActive(true);
        route.setImageData(imageDataRepository.findById(routeCreateDto.getImageDataId())
                .orElseThrow(() -> new EntityNotFoundException("Image with id[" + routeCreateDto.getImageDataId() + "] not found")));
    }
}
