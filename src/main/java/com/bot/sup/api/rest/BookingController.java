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

    @GetMapping("{scheduleId}")
    @Operation(summary = "Получить записи по scheduleId")
    public ResponseEntity<List<BookingCreateDto>> getBookingByScheduleId(@PathVariable(name = "scheduleId") Long scheduleId) {
        List<BookingCreateDto> bookings = bookingService.getBookingByScheduleId(scheduleId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Получить записи по scheduleId и paymentStatus ")
    public ResponseEntity<List<BookingCreateDto>> getBookingByScheduleIdByPaymentStatus(@RequestParam(value = "id") String id,
                                                                                        @RequestParam(value = "paymentStatus") String paymentStatus){
        List<BookingCreateDto> bookings = bookingService.getBookingByScheduleIdByPaymentStatus(Long.valueOf(id), paymentStatus);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<String>> getPaymentType() {
        List<String> types = PaymentTypeEnum.getTitles();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<String>> getPaymentStatus() {
        List<String> statuses = PaymentStatusEnum.getTitles();
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @GetMapping("/freePlace/{scheduleId}")
    public ResponseEntity<Integer> getFreePlace(@PathVariable(name = "scheduleId") Long scheduleId){
        Integer freePlace = bookingService.getCountFreePlaces(scheduleId);
        return new ResponseEntity<>(freePlace, HttpStatus.OK);
    }

}
