package com.bot.sup.api.rest;

import com.bot.sup.common.enums.PaymentStatusEnum;
import com.bot.sup.common.enums.PaymentTypeEnum;
import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.service.booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/booking")
@Tag(name = "Бронь")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("{scheduleId}")
    @ResponseStatus
    @Operation(summary = "Получить записи по scheduleId")
    public List<BookingDto> getBookingByScheduleId(@PathVariable(name = "scheduleId") Long scheduleId) {
        return bookingService.getBookingByScheduleId(scheduleId);
    }

    @GetMapping
    @Operation(summary = "Получить записи по scheduleId и paymentStatus ")
    public List<BookingDto> getBookingByScheduleIdByPaymentStatus(@RequestParam(value = "id") String id,
                                                                  @RequestParam(value = "paymentStatus") String paymentStatus) {
        return bookingService.getBookingByScheduleIdByPaymentStatus(Long.valueOf(id), paymentStatus);
    }

    @GetMapping("/type")
    public List<String> getPaymentType() {
        Map<PaymentTypeEnum, String> titles = new HashMap<>();
        return PaymentTypeEnum.getTitles();
    }

    @GetMapping("/status")
    public List<String> getPaymentStatus() {
        return PaymentStatusEnum.getTitles();
    }

    @GetMapping("/freePlace/{scheduleId}")
    public Integer getFreePlace(@PathVariable(name = "scheduleId") Long scheduleId) {
        return bookingService.getCountFreePlaces(scheduleId);
    }

    @PostMapping
    @Operation(summary = "Создать ноую бронь")
    public BookingDto createBooking (@RequestBody BookingDto bookingDto){
        return null;
    }

}
