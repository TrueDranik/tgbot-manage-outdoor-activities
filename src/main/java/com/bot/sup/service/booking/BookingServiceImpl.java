package com.bot.sup.service.booking;

import com.bot.sup.mapper.BookingMapper;
import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.BookingRepository;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    private final ScheduleRepository scheduleRepository;

    private final ClientRepository clientRepository;

    @Override
    public List<BookingDto> getBookingByScheduleId(Long scheduleId) {
        List<Booking> bookings = bookingRepository.findBookingByScheduleId(scheduleId);
        return bookingMapper.domainsToDtos(bookings);
    }

    @Override
    public List<BookingDto> getBookingByScheduleIdByPaymentStatus(Long scheduleId, String paymentStatus) {
        List<Booking> bookings = bookingRepository.findBookingByScheduleIdByPaymentStatus(scheduleId, paymentStatus);
        return bookingMapper.domainsToDtos(bookings);
    }

    @Override
    public Integer getCountFreePlaces(Long scheduleId) {
        List<Booking> bookings = bookingRepository.findBookingByScheduleId(scheduleId);
        Schedule schedule = scheduleRepository.getSchedulesById(scheduleId);
        if (bookings.isEmpty()) {
            return schedule.getParticipants();
        }

        int countInvatedUsers = 0;
        for (Booking booking : bookings) {
            countInvatedUsers += booking.getInvitedUsers() + booking.getInvitedChildren();
        }

        return schedule.getParticipants() - countInvatedUsers;
    }

    @Override
    public void createBooking(BookingDto bookingDto) {

    }
}
