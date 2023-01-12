package com.bot.sup.mapper;

import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.model.entity.Booking;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<Booking, BookingDto>{

    @Override
    List<BookingDto> domainsToDtos(List<Booking> domains);
}
