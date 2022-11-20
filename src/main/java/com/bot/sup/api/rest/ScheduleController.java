package com.bot.sup.api.rest;

import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.activity.ActivityService;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleMapper scheduleMapper;
    private final ScheduleService scheduleService;
    private final ActivityService activityService;
    private final ScheduleRepository scheduleRepository;

    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedule() {
        return new ResponseEntity<>(scheduleService.getAllSchedule(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable("id") Long id) {
        Optional<Schedule> schedule = Optional.ofNullable(scheduleService.getScheduleById(id));

        if (schedule.isPresent()) {
            return new ResponseEntity<>(schedule.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleCreateDto scheduleCreateDto) {
        return new ResponseEntity<>(scheduleService.createSchedule(scheduleCreateDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable(name = "id") Long id,
                                                   @RequestBody ScheduleCreateDto scheduleCreateDto) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, scheduleCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Schedule> deleteSchedule(@PathVariable(name = "id") Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
