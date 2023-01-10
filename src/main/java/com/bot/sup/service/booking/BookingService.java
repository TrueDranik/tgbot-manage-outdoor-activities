package com.bot.sup.service.booking;

import com.bot.sup.model.dto.BookingCreateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<BookingCreateDto> getPaymentTypes();

    List<BookingCreateDto> getPaymentStatus();

    List<BookingCreateDto> getBookingByPaymentStatus();

    BookingCreateDto createClient();

    BookingCreateDto updateBooking();

}
