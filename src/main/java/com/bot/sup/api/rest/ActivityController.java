package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.service.activity.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController("activityController")
@RequestMapping(value = "/activity")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    @ApiOperation("Получить все активности")
    public ResponseEntity<List<Activity>> getAllActivity() {
        return new ResponseEntity<>(activityService.getAllActivity(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable(name = "id") Long id) {
        Optional<Activity> activity = Optional.ofNullable(activityService.getActivityById(id));

        if (activity.isPresent()) {
            return new ResponseEntity<>(activity.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityCreateDto createDto) {
        return new ResponseEntity<>(activityService.createActivity(createDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable(name = "id") Long id,
                                                   @RequestBody ActivityCreateDto createDto) {
        return new ResponseEntity<>(activityService.updateActivity(id, createDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Activity> deleteActivity(@PathVariable(name = "id") Long id) {
        activityService.deleteActivity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
