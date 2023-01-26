package com.bot.sup.mapper;

import com.bot.sup.model.dto.RouteDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.ImageDataRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;

@Mapper(componentModel = "spring")
public abstract class RouteMapper implements BaseMapper<Route, RouteDto> {
    @Autowired
    ImageDataRepository imageDataRepository;

    @AfterMapping
    public void setRepoValues(RouteDto dto, @MappingTarget Route route) {
        route.setIsActive(true);
        route.setImageData(imageDataRepository.findById(dto.getImageDataId())
                .orElseThrow(() -> new EntityNotFoundException("Image with id[" + dto.getImageDataId() + "] not found")));
    }
}
