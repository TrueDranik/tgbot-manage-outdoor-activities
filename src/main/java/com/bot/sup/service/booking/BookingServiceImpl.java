package com.bot.sup.service.booking;

import com.bot.sup.common.enums.PaymentStatusEnum;
import com.bot.sup.mapper.BookingMapper;
import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.model.dto.BookingUpdateDto;
import com.bot.sup.model.dto.BookingsSortedByPaymentStatusDto;
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

    public Booking findBookingByClientAndByScheduleId(Long scheduleId, Long clientId) {
        return bookingRepository
                .findBookingByClientAndByScheduleId(scheduleId, clientId)
                .orElseThrow(() -> new EntityNotFoundException("Invited users with client id[" + clientId + "] not found"));
    }

    public Long findSumBookingClientByScheduleId(Long scheduleId) {
        return bookingRepository.findSumBookingClientByScheduleId(scheduleId);
    }

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
        List<Booking> activeBookings = bookingRepository.findBookingByScheduleIdAndActiveIsTrue(scheduleId);
        Schedule schedule = scheduleRepository.getSchedulesById(scheduleId);
        if (activeBookings.isEmpty()) {
            return schedule.getParticipants();
        }

        int countInvatedUsers = 0;
        for (Booking booking : activeBookings) {
            countInvatedUsers += booking.getInvitedUsers() + booking.getInvitedChildren();
        }

        return schedule.getParticipants() - countInvatedUsers;
    }

    @Override
    public BookingDto createBooking(BookingCreateDto bookingcreateDto) {
        Client client = clientRepository.findByPhoneNumber(bookingcreateDto.getPhoneNumber());
        if (client != null) {
            Integer freePlace = getCountFreePlaces((bookingcreateDto.getScheduleId()));
            if (freePlace <= 0) {
                throw new IllegalArgumentException("Free place is 0");
            }
            Schedule schedule = scheduleRepository.getSchedulesById(bookingcreateDto.getScheduleId());
            Booking booking = bookingMapper.dtoToDomain(bookingcreateDto);
            booking.setClient(client);
            booking.setSchedule(schedule);
            booking.setInsTime(LocalDate.now());
            booking.setModifTime(LocalDate.now());
            bookingRepository.save(booking);
            return bookingMapper.domainToDto(booking);
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
        bookingToUpdate.setModifTime(LocalDate.now());
        Schedule schedule = scheduleRepository.getSchedulesById(bookingUpdateDto.getScheduleId());
        bookingToUpdate.setSchedule(schedule);
        bookingRepository.save(bookingToUpdate);

        return bookingMapper.domainToDto(bookingToUpdate);
    }

    @Override
    public BookingsSortedByPaymentStatusDto getAllBookingsByScheduleIdByPaymentStatus(Long scheduleId) {
        BookingsSortedByPaymentStatusDto sortedByPaymentStatusDto = new BookingsSortedByPaymentStatusDto();
        sortedByPaymentStatusDto.setPaidBookings(getBookingByScheduleIdByPaymentStatus(scheduleId, PaymentStatusEnum.PAID.name()));
        sortedByPaymentStatusDto.setNotPaidBookings(getBookingByScheduleIdByPaymentStatus(scheduleId, PaymentStatusEnum.NOT_PAID.name()));
        sortedByPaymentStatusDto.setReturnedBookings(getBookingByScheduleIdByPaymentStatus(scheduleId, PaymentStatusEnum.RETURNED.name()));
        sortedByPaymentStatusDto.setRefundRequestedBookings(getBookingByScheduleIdByPaymentStatus(scheduleId, PaymentStatusEnum.REFUND_REQUESTED.name()));
        sortedByPaymentStatusDto.setCancelWithoutRefundBookings(getBookingByScheduleIdByPaymentStatus(scheduleId, PaymentStatusEnum.CANCEL_WITHOUT_REFUND.name()));

        return sortedByPaymentStatusDto;
    }

}
