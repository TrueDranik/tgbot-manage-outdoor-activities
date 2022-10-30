package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityTypeCreateDto;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.service.activity.type.ActivityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activity_type")
@RequiredArgsConstructor
public class ActivityTypeController {
    private final ActivityTypeService activityTypeService;

    @GetMapping
    public ResponseEntity<List<ActivityType>> getAllActivityType() {
        return new ResponseEntity<>(activityTypeService.getAllActivityType(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityType> getActivityTypeById(@PathVariable(name = "id") Long id) {
        Optional<ActivityType> activityType = Optional.ofNullable(activityTypeService.getActivityTypeById(id));

        if (activityType.isPresent()) {
            return new ResponseEntity<>(activityType.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<ActivityType> createActivityType(@RequestBody ActivityTypeCreateDto activityTypeCreateDto) {
        return new ResponseEntity<>(activityTypeService.createActivityType(activityTypeCreateDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ActivityType> updateActivityType(@PathVariable(name = "id") Long id,
                                                           @RequestBody ActivityTypeCreateDto activityFormatCreateDto) {
        return new ResponseEntity<>(activityTypeService.updateActivityType(id, activityFormatCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ActivityType> deleteActivityType(@PathVariable(name = "id") Long id) {
        activityTypeService.deleteActivityType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
