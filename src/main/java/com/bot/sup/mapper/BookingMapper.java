package com.bot.sup.mapper;

import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.entity.Booking;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<Booking, BookingCreateDto>{

    @Override
    List<BookingCreateDto> domainsToDtos(List<Booking> domains);
}
