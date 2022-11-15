package com.bot.sup.api.rest;

import com.bot.sup.config.DocumentedOperation;
import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.service.activity.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

//@CrossOrigin("https://tgsupbot.reliab.tech/admin/api/")
@RequiredArgsConstructor
@RestController("activityController")
@RequestMapping(value = "/activity")
@Tag(name = "Активности", description = "Работа с активностями")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    @Operation(summary = "Получить все активности")
    @DocumentedOperation(desc = "Get page with Activities", errors = {BAD_REQUEST})
    public ResponseEntity<List<Activity>> getAllActivity(ActivityRequestParams params) {
        return new ResponseEntity<>(activityService.getAllActivity(params), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Activity> getActivityById(@Parameter(description = "Id") @PathVariable(name = "id") Long id) {
        Optional<Activity> activity = Optional.ofNullable(activityService.getActivityById(id));

        return activity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
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
