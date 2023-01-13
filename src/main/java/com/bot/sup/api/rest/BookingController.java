package com.bot.sup.api.rest;

import com.bot.sup.common.enums.PaymentStatusEnum;
import com.bot.sup.common.enums.PaymentTypeEnum;
import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.dto.BookingDto;
import com.bot.sup.model.dto.BookingUpdateDto;
import com.bot.sup.service.booking.BookingService;
import com.bot.sup.service.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/booking")
@Tag(name = "Бронь")
public class BookingController {
    private final BookingService bookingService;
    private final ClientService clientService;

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
    public Map<PaymentTypeEnum, String> getPaymentType() {

        return PaymentTypeEnum.getTitles();
    }

    @GetMapping("/status")
    public Map<PaymentStatusEnum, String> getPaymentStatus() {
        return PaymentStatusEnum.getTitles();
    }

    @GetMapping("/freePlace/{scheduleId}")
    public Integer getFreePlace(@PathVariable(name = "scheduleId") Long scheduleId) {
        return bookingService.getCountFreePlaces(scheduleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать ноую бронь")
    public BookingDto createBooking(@RequestBody BookingCreateDto bookingCreateDto) {
        clientService.createClient(bookingCreateDto);
        return bookingService.createBooking(bookingCreateDto);
    }

    @PutMapping
    @Operation(summary = "Обновление букинга")
    public BookingDto updateBooking(@RequestBody BookingUpdateDto bookingUpdateDto) {
        return bookingService.updateBooking(bookingUpdateDto);
    }

}
