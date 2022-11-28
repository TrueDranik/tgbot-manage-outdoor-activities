package com.bot.sup.api.rest;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
@Tag(name = "Расписание")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    @Operation(summary = "Получить список всех расписаний")
    public ResponseEntity<List<Schedule>> getAllSchedule(@ParameterObject ScheduleRequestParams params) {
        return new ResponseEntity<>(scheduleService.getAllSchedule(params), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить расписание по id")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable("id") Long id) {
        Optional<Schedule> schedule = Optional.ofNullable(scheduleService.getScheduleById(id));

        return schedule.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping
    @Operation(summary = "Создать новое расписание")
    public ResponseEntity<List<Schedule>> createSchedule(@RequestBody List<ScheduleCreateDto> scheduleCreateDto) {
        return new ResponseEntity<>(scheduleService.createSchedule(scheduleCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующее расписание")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable(name = "id") Long id,
                                                   @RequestBody ScheduleCreateDto scheduleCreateDto) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, scheduleCreateDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующее расписание")
    public ResponseEntity<ScheduleCreateDto> deleteSchedule(@PathVariable(name = "id") Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
