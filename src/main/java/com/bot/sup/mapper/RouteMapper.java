package com.bot.sup.mapper;

import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.entity.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteMapper extends BaseMapper<Route, RouteCreateDto> {

}
