package com.bot.sup.service.booking;

import com.bot.sup.mapper.BookingMapper;
import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    public List<BookingCreateDto> getBookingByScheduleId(Long scheduleId) {
        List<Booking> bookings = bookingRepository.findBookingBySchedule(scheduleId);
        return bookingMapper.domainsToDtos(bookings);
    }

    @Override
    public List<BookingCreateDto> getBookingByScheduleIdByPaymentStatus(Long scheduleId, String paymentStatus) {
        List<Booking> bookings = bookingRepository.findBookingByScheduleIdByPaymentStatus(scheduleId, paymentStatus);
        return bookingMapper.domainsToDtos(bookings);
    }
}
