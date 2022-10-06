package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityDto;
import com.bot.sup.model.dto.RouteDto;
import com.bot.sup.model.dto.ScheduleDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.NewTourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class NewTourController {
    private final NewTourService newTourService;
    private final ScheduleRepository scheduleRepository;

    @PostMapping("/new")
    public void setNewTour(@RequestBody ScheduleDto scheduleDto) {
        newTourService.save(scheduleDto);
    }

    @PostMapping("/change")
    public void setChangeTour(@RequestBody ScheduleDto scheduleDto){

    }

    @GetMapping("/dto")
    public Schedule getNewTour() {
        List<Schedule> all = scheduleRepository.findAll();
        Schedule schedule = all.get(0);
        ScheduleDto scheduleDto = new ScheduleDto();
        ActivityDto activityDto = new ActivityDto();
        RouteDto routeDto = new RouteDto();

        routeDto.setDescription("dsfsdfs");

        activityDto.setStartPointName("hui_start");
        activityDto.setFinishPointName("hui_finifh");
        activityDto.setRouteDto(routeDto);

        LocalDateTime localDateTime = LocalDateTime.now();

        scheduleDto.setId(1L);
        scheduleDto.setEventDate(localDateTime);
        scheduleDto.setActivityDto(activityDto);

        return schedule;
    }
}
