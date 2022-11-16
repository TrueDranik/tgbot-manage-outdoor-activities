package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.activity.format.ActivityFormatService;
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
public class ActivityFormatController {
    private final ActivityFormatService activityFormatService;

    @GetMapping
    public ResponseEntity<List<ActivityFormat>> getAllActivityFormat() {
        return new ResponseEntity<>(activityFormatService.getAllActivityFormat(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityFormat> getActivityFormatById(@PathVariable(name = "id") Long id) {
        Optional<ActivityFormat> activityFormat = Optional.ofNullable(activityFormatService.getActivityFormatById(id));

        if (activityFormat.isPresent()) {
            return new ResponseEntity<>(activityFormat.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<ActivityFormat> createActivityFormat(@RequestBody ActivityFormatCreateDto activityFormatCreateDto) {
        return new ResponseEntity<>(activityFormatService.createActivityFormat(activityFormatCreateDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ActivityFormat> updateActivityFormat(@PathVariable(name = "id") Long id,
                                                               @RequestBody ActivityFormatCreateDto activityFormatCreateDto) {
        return new ResponseEntity<>(activityFormatService.updateActivityFormat(id, activityFormatCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ActivityFormat> deleteActivityFormat(@PathVariable(name = "id") Long id) {
        activityFormatService.deleteActivityFormat(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
