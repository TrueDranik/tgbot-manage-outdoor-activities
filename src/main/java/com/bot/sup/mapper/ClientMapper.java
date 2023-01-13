package com.bot.sup.mapper;

import com.bot.sup.model.dto.ClientDto;
import com.bot.sup.model.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends BaseMapper<Client, ClientDto>{
}
