package com.bot.sup.mapper;

import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.entity.Route;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteMapper extends BaseMapper<Route, RouteCreateDto> {
    @Override
    RouteCreateDto domainToDto(Route domain);

    @Override
    Route dtoToDomain(RouteCreateDto routeCreateDto);

    @Override
    List<RouteCreateDto> domainsToDtos(List<Route> domains);

    @Override
    List<Route> dtosToDomains(List<RouteCreateDto> routeCreateDtos);
}
