package com.bot.sup.api.rest;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.dto.ScheduleDto;
import com.bot.sup.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
@Tag(name = "Расписание")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/telegramId/{telegramId}")
    @Operation(summary = "Получить расписание, выбранное пользователем")
    public ScheduleDto getScheduleByTelegramId(@PathVariable("telegramId") Long telegramId) {
        return scheduleService.getScheduleByTelegramId(telegramId);
    }

    @GetMapping
    @Operation(summary = "Получить список всех расписаний")
    public List<ScheduleDto> getAllSchedule(@ParameterObject ScheduleRequestParams params) {
        return scheduleService.getAllSchedule(params);
    }

    @GetMapping("/scheduleParams")
    @Operation(summary = "Get all filtered schedules")
    public List<ScheduleDto> getAllFilteredSchedule(@RequestParam(value = "active") Boolean isActive,
                                                    @RequestParam(value = "date") String eventDate,
                                                    @RequestParam(value = "time") String eventTime) {
        return scheduleService.getAllFilteredSchedule(isActive, LocalDate.parse(eventDate), LocalTime.parse(eventTime));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить расписание по id")
    public ScheduleDto getScheduleById(@PathVariable("id") Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Создать новое расписание")
    public List<ScheduleCreateDto> createSchedule(@RequestBody List<ScheduleCreateDto> scheduleCreateDto) {
        return scheduleService.createSchedule(scheduleCreateDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующее расписание")
    public ScheduleCreateDto updateSchedule(@PathVariable(name = "id") Long id,
                                            @RequestBody ScheduleCreateDto scheduleCreateDto) {
        return scheduleService.updateSchedule(id, scheduleCreateDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующее расписание")
    public void deleteSchedule(@PathVariable(name = "id") Long id) {
        scheduleService.deleteSchedule(id);
    }
}
