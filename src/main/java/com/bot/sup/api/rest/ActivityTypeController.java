package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityTypeDto;
import com.bot.sup.service.activity.type.ActivityTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/activity_type")
@RequiredArgsConstructor
@Tag(name = "Тип активности")
public class ActivityTypeController {
    private final ActivityTypeService activityTypeService;

    @GetMapping
    @Operation(summary = "Получить список всех типов активности", method = "GET")
    public List<ActivityTypeDto> getAllActivityType() {
        return activityTypeService.getAllActivityType();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип активности по id")
    public ActivityTypeDto getActivityTypeById(@PathVariable(name = "id") Long id) {
        return activityTypeService.getActivityTypeById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый тип активности")
    public ActivityTypeDto createActivityType(@RequestBody ActivityTypeDto activityTypeDto) {
        return activityTypeService.createActivityType(activityTypeDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующий тип активности")
    public ActivityTypeDto updateActivityType(@PathVariable(name = "id") Long id,
                                              @RequestBody ActivityTypeDto activityTypeDto) {
        return activityTypeService.updateActivityType(id, activityTypeDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующий тип активности")
    public void deleteActivityType(@PathVariable(name = "id") Long id) {
        activityTypeService.deleteActivityType(id);
    }
}
