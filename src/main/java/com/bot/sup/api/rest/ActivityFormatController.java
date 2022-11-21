package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.activity.format.ActivityFormatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/activity_format")
@RequiredArgsConstructor
@Tag(name = "Формат активности")
public class ActivityFormatController {
    private final ActivityFormatService activityFormatService;

    @GetMapping
    @Operation(summary = "Получить список всех форматов активности")
    public ResponseEntity<List<ActivityFormat>> getAllActivityFormat() {
        return new ResponseEntity<>(activityFormatService.getAllActivityFormat(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить формат активности по id")
    public ResponseEntity<ActivityFormat> getActivityFormatById(@PathVariable(name = "id") Long id) {
        Optional<ActivityFormat> activityFormat = Optional.ofNullable(activityFormatService.getActivityFormatById(id));

        return activityFormat.map(format -> new ResponseEntity<>(format, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping
    @Operation(summary = "Создать новый формат активности")
    public ResponseEntity<ActivityFormat> createActivityFormat(@RequestBody ActivityFormatCreateDto activityFormatCreateDto) {
        return new ResponseEntity<>(activityFormatService.createActivityFormat(activityFormatCreateDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующий формат активности")
    public ResponseEntity<ActivityFormat> updateActivityFormat(@PathVariable(name = "id") Long id,
                                                               @RequestBody ActivityFormatCreateDto activityFormatCreateDto) {
        return new ResponseEntity<>(activityFormatService.updateActivityFormat(id, activityFormatCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующий формат активности")
    public ResponseEntity<ActivityFormat> deleteActivityFormat(@PathVariable(name = "id") Long id) {
        activityFormatService.deleteActivityFormat(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
