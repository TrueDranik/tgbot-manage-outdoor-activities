package com.bot.sup.service.booking;

import com.bot.sup.mapper.BookingMapper;
import com.bot.sup.mapper.ClientMapper;
import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.mapper.ScheduleMapperImpl;
import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.model.dto.BookingUpdateDto;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.model.entity.Client;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.BookingRepository;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    private final ScheduleRepository scheduleRepository;

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    private final ScheduleMapper scheduleMapper;

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
    public BookingDto createBooking(BookingCreateDto bookingcreateDto) {
        Client client = clientRepository.findByPhoneNumber(bookingcreateDto.getPhoneNumber());
        if (client != null) {
            Booking booking = new Booking();
            booking.setClient(client);
            booking.setInvitedUsers(bookingcreateDto.getInvitedUsers());
            booking.setInvitedChildren(bookingcreateDto.getInvitedChildren());
            booking.setPaymentStatus(bookingcreateDto.getPaymentStatus());
            booking.setPaymentType(bookingcreateDto.getPaymentType());
            booking.setActive(true);
            Schedule schedule = scheduleRepository.getSchedulesById(bookingcreateDto.getScheduleId());
            booking.setSchedule(schedule);
            booking.setInsTime(LocalDate.now());
            booking.setModifTime(LocalDate.now());
            booking.setAmountPaid(bookingcreateDto.getAmountPaid());
            bookingRepository.save(booking);
            BookingDto bookingDto = bookingMapper.domainToDto(booking);
           // bookingDto.setScheduleId(bookingcreateDto.getScheduleId());
            bookingDto.setScheduleId(scheduleMapper.domainToDto(schedule));
            bookingDto.setClient(clientMapper.domainToDto(client));
            return bookingDto;
        }
        throw new EntityNotFoundException("Client with phone number " + bookingcreateDto.getPhoneNumber() + " not found");
    }

    @Override
    public BookingDto updateBooking(BookingUpdateDto bookingUpdateDto) {
        Booking bookingToUpdate = bookingRepository.findBookingById(bookingUpdateDto.getId());
        if (bookingToUpdate == null) {
            throw new EntityNotFoundException("Booking with Id " + bookingUpdateDto.getId() + " not found");
        }
        bookingToUpdate = bookingMapper.dtoToDomain(bookingUpdateDto);
        Client client = clientRepository.findById(bookingUpdateDto.getClientId().getId()).get();
        bookingToUpdate.setClient(client);
        bookingToUpdate.setModifTime(LocalDate.now());
        Schedule schedule = scheduleRepository.getSchedulesById(bookingUpdateDto.getScheduleId());
        bookingToUpdate.setSchedule(schedule);
        bookingRepository.save(bookingToUpdate);
        BookingDto bookingDto = bookingMapper.domainToDto(bookingToUpdate);
        bookingDto.setScheduleId(scheduleMapper.domainToDto(schedule));

        return bookingDto;
    }
}
