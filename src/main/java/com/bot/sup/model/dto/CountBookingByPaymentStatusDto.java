package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountBookingByPaymentStatusDto {
    Integer paidBookings;
    Integer notPaidBookings;
    Integer refundRequestedBookings;
    Integer cancelWithoutRefundBookings;
    Integer returnedBookings;
    Integer prepaidBookings;

}
