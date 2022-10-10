package com.bot.sup.api.rest;

import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.dto.ScheduleDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.activity.ActivityService;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@CrossOrigin(value = "https://192.168.110.84:3000/")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleMapper scheduleMapper;
    private final ScheduleService scheduleService;
    private final ActivityService activityService;

    @Autowired
    private final ScheduleRepository scheduleRepository;

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getAllSchedule(@RequestParam(required = false) Long id) {
        try {
            List<Schedule> schedules = new ArrayList<>();

            if (id == null) {
                schedules.addAll(scheduleRepository.findAll());
            } else {
                schedules.addAll(scheduleRepository.findAllById(Collections.singleton(id)));
            }

            if (schedules.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable("id") Long id) {
        Optional<Schedule> schedule = Optional.ofNullable(scheduleService.getScheduleById(id));

        if (schedule.isPresent()) {
            return new ResponseEntity<>(schedule.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/schedules")
    public void createSchedule(@RequestBody ScheduleCreateDto scheduleCreateDto) {
        scheduleService.createSchedule(scheduleCreateDto);
//        try {
//
//            ScheduleDto scheduleResponse = modelMapper.map(schedule, ScheduleDto.class);
//
//            return new ResponseEntity<>(scheduleResponse, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
}
