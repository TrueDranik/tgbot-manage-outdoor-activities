package com.bot.sup.mapper;

import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.dto.RouteDto;
import com.bot.sup.model.entity.Route;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteMapper extends BaseMapper<Route, RouteCreateDto> {
    List<RouteDto> routesToDtos(List<Route> routes);
}
