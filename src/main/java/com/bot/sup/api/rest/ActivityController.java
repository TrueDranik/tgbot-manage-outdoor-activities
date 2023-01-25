package com.bot.sup.api.rest;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityDto;
import com.bot.sup.service.activity.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController("activityController")
@RequestMapping(value = "/activity")
@Tag(name = "Активность")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    @Operation(summary = "Получить список всех активностей")
    public List<ActivityDto> getAllActivity(@ParameterObject ActivityRequestParams params) {
        return activityService.getAllActivity(params);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить активность по id")
    public ActivityDto getActivityById(@Parameter(description = "Id") @PathVariable(name = "id") Long id) {
        return activityService.getActivityById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую активность")
    public ActivityDto createActivity(@RequestBody ActivityDto activityDto) {
        return activityService.createActivity(activityDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующую активность")
    public ActivityDto updateActivity(@PathVariable(name = "id") Long id,
                                      @RequestBody ActivityDto activityDto) {
        return activityService.updateActivity(id, activityDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Изменить активность")
    public void deleteActivity(@PathVariable(name = "id") Long id) {
        activityService.deleteActivity(id);
    }
}
