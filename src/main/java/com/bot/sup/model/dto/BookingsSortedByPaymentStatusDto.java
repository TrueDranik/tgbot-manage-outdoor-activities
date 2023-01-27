package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingsSortedByPaymentStatusDto {
    List<BookingDto> paidBookings;
    List<BookingDto> notPaidBookings;
    List<BookingDto> refundRequestedBookings;
    List<BookingDto> cancelWithoutRefundBookings;
    List<BookingDto> returnedBookings;
    List<BookingDto> prepaidBookings;

}
