package com.bot.sup.mapper;

import java.util.List;
//TODO: Удалить override методы
public interface BaseMapper<D, DTO>  {
    DTO domainToDto(D domain);

    D dtoToDomain(DTO dto);

    List<DTO> domainsToDtos(List<D> domains);

    List<D> dtosToDomains(List<DTO> dtos);
}
