package com.bot.sup.service.booking;

import com.bot.sup.common.enums.PaymentTypeEnum;
import com.bot.sup.model.dto.BookingCreateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<BookingCreateDto> getBookingByScheduleId(Long scheduleId);

    List<BookingCreateDto> getBookingByScheduleIdByPaymentStatus(Long scheduleId, String paymentStatus);

    Integer getCountFreePlaces(Long scheduleId);

}
