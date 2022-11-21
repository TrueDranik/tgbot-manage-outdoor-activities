package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityTypeCreateDto;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.service.activity.type.ActivityTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/activity_type")
@RequiredArgsConstructor
@Tag(name = "Тип активности")
public class ActivityTypeController {
    private final ActivityTypeService activityTypeService;

    @GetMapping
    @Operation(summary = "Получить список всех типов активности", method = "GET")
    public ResponseEntity<List<ActivityType>> getAllActivityType() {
        return new ResponseEntity<>(activityTypeService.getAllActivityType(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип активности по id")
    public ResponseEntity<ActivityType> getActivityTypeById(@PathVariable(name = "id") Long id) {
        Optional<ActivityType> activityType = Optional.ofNullable(activityTypeService.getActivityTypeById(id));

        return activityType.map(type -> new ResponseEntity<>(type, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping
    @Operation(summary = "Создать новый тип активности")
    public ResponseEntity<ActivityType> createActivityType(@RequestBody ActivityTypeCreateDto activityTypeCreateDto) {
        return new ResponseEntity<>(activityTypeService.createActivityType(activityTypeCreateDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующий тип активности")
    public ResponseEntity<ActivityType> updateActivityType(@PathVariable(name = "id") Long id,
                                                           @RequestBody ActivityTypeCreateDto activityFormatCreateDto) {
        return new ResponseEntity<>(activityTypeService.updateActivityType(id, activityFormatCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующий тип активности")
    public ResponseEntity<ActivityType> deleteActivityType(@PathVariable(name = "id") Long id) {
        activityTypeService.deleteActivityType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
