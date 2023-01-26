package com.bot.sup.service.booking;

import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.model.dto.BookingUpdateDto;
import com.bot.sup.model.dto.BookingsSortedByPaymentStatusDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<BookingDto> getBookingByScheduleId(Long scheduleId);

    List<BookingDto> getBookingByScheduleIdByPaymentStatus(Long scheduleId, String paymentStatus);

    Integer getCountFreePlaces(Long scheduleId);

    BookingDto createBooking (BookingCreateDto bookingCreateDto);

    BookingDto updateBooking (BookingUpdateDto bookingUpdateDto);

    BookingsSortedByPaymentStatusDto getAllBookingsByScheduleIdByPaymentStatus(Long scheduleId);

}
