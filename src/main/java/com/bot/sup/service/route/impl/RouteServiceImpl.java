package com.bot.sup.service.route.impl;

import com.bot.sup.mapper.RouteMapper;
import com.bot.sup.model.dto.RouteDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Route_;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final RouteMapper routeMapper;

    @Override
    public List<RouteDto> getAllRoute() {
        List<Route> routes = routeRepository.findAll();

        return routeMapper.domainsToDtos(routes);
    }

    @Override
    public RouteDto getRouteById(Long id) {
        Route routeById = findRouteById(id);

        return routeMapper.domainToDto(routeById);
    }

    @Override
    public RouteDto createRoute(RouteDto routeDto) {
        Route route = routeMapper.dtoToDomain(routeDto);
        routeRepository.save(route);

        return routeMapper.domainToDto(route);
    }

    @Override
    public RouteDto updateRoute(Long id, RouteDto routeDto) {
        Route routeForUpdate = routeMapper.dtoToDomain(routeDto);
        Route routeFromDb = findRouteById(id);

        BeanUtils.copyProperties(routeForUpdate, routeFromDb, Route_.ID, Route_.IS_ACTIVE);
        routeRepository.save(routeFromDb);

        return routeMapper.domainToDto(routeFromDb);
    }

    @Transactional
    @Override
    public void deleteRoute(Long id) {
        Route routeById = findRouteById(id);
        routeById.setIsActive(false);
        routeRepository.save(routeById);

        scheduleRepository.setScheduleInactiveByRouteId(id);
    }

    private Route findRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with id[" + id + "] not found"));
    }
}
