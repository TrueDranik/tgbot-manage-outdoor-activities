package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityFormatDto;
import com.bot.sup.service.activity.format.ActivityFormatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/activity_format")
@RequiredArgsConstructor
@Tag(name = "Формат активности")
public class ActivityFormatController {
    private final ActivityFormatService activityFormatService;

    @GetMapping
    @Operation(summary = "Получить список всех форматов активности")
    public List<ActivityFormatDto> getAllActivityFormat() {
        return activityFormatService.getAllActivityFormat();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить формат активности по id")
    public ActivityFormatDto getActivityFormatById(@PathVariable(name = "id") Long id) {
        return activityFormatService.getActivityFormatById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый формат активности")
    public ActivityFormatDto createActivityFormat(@RequestBody ActivityFormatDto activityFormatDto) {
        return activityFormatService.createActivityFormat(activityFormatDto);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Изменить существующий формат активности")
    public ActivityFormatDto updateActivityFormat(@PathVariable(name = "id") Long id,
                                                  @RequestBody ActivityFormatDto activityFormatDto) {
        return activityFormatService.updateActivityFormat(id, activityFormatDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующий формат активности")
    public void deleteActivityFormat(@PathVariable(name = "id") Long id) {
        activityFormatService.deleteActivityFormat(id);
    }
}
