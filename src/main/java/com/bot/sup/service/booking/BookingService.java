package com.bot.sup.service.booking;

import com.bot.sup.model.dto.BookingDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<BookingDto> getBookingByScheduleId(Long scheduleId);

    List<BookingDto> getBookingByScheduleIdByPaymentStatus(Long scheduleId, String paymentStatus);

    Integer getCountFreePlaces(Long scheduleId);

    void createBooking (BookingDto bookingDto);

}
