package com.bot.sup.api.rest;

import com.bot.sup.common.enums.PaymentStatusEnum;
import com.bot.sup.common.enums.PaymentTypeEnum;
import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.service.booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/booking")
@Tag(name = "Бронь")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("{id}")
    @Operation(summary = "Получить записи по scheduleId")
    public ResponseEntity<List<BookingCreateDto>> getBookingByScheduleId(@Parameter(description = "Id")
                                                                         @PathVariable(name = "id") Long id) {
        List<BookingCreateDto> bookings = bookingService.getBookingByScheduleId(id);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Получить записи по scheduleId и paymentStatus ")
    public ResponseEntity<List<BookingCreateDto>> getBookingByScheduleIdByPaymentStatus(@RequestParam(value = "id") Long id,
                                                                                        @RequestParam(value = "paymentStatus") String paymentStatus){
        List<BookingCreateDto> bookings = bookingService.getBookingByScheduleIdByPaymentStatus(id, paymentStatus);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<PaymentTypeEnum>> getPaymentType() {
        PaymentTypeEnum[] types = PaymentTypeEnum.values();
        return new ResponseEntity<>(Arrays.asList(types), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<String>> getPaymentStatus() {
        List<String> statuses = PaymentStatusEnum.getTitles();
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

}
