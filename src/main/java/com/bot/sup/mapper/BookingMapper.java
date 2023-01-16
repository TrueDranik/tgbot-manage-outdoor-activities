package com.bot.sup.mapper;

import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.model.dto.BookingUpdateDto;
import com.bot.sup.model.dto.ClientCreateDto;
import com.bot.sup.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<Booking, BookingDto> {

    @Override
    List<BookingDto> domainsToDtos(List<Booking> domains);

    @Override
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "schedule", target = "scheduleId")
    BookingDto domainToDto(Booking domain);

    ClientCreateDto dtoToDto(BookingCreateDto bookingCreateDto);


    Booking dtoToDomain(BookingUpdateDto bookingUpdateDto);
}
