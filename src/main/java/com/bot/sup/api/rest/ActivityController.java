package com.bot.sup.api.rest;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.dto.ActivityDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.service.activity.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RestController("activityController")
@RequestMapping(value = "/activity")
@Tag(name = "Активность")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    @Operation(summary = "Получить список всех активностей")
    public ResponseEntity<List<ActivityDto>> getAllActivity(@ParameterObject ActivityRequestParams params) {
        //TODO возвращать dto, а не entity
        return new ResponseEntity<>(activityService.getAllActivity(params), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить активность по id")
    public ResponseEntity<Activity> getActivityById(@Parameter(description = "Id") @PathVariable(name = "id") Long id) {
        Optional<Activity> activity = Optional.ofNullable(activityService.getActivityById(id));

        return activity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping
    @Operation(summary = "Создать новую активность")
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityCreateDto createDto) {
        return new ResponseEntity<>(activityService.createActivity(createDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующую активность")
    public ResponseEntity<Activity> updateActivity(@PathVariable(name = "id") Long id,
                                                   @RequestBody ActivityCreateDto createDto) {
        return new ResponseEntity<>(activityService.updateActivity(id, createDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Изменить активность")
    public ResponseEntity<Activity> deleteActivity(@PathVariable(name = "id") Long id) {
        activityService.deleteActivity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
