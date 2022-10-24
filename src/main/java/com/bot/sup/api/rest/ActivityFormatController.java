package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.activityFormat.ActivityFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activity_format")
@RequiredArgsConstructor
public class ActivityFormatController {
    private final ActivityFormatService activityFormatService;

    @GetMapping("/all")
    public ResponseEntity<List<ActivityFormat>> getAllActivityFormat() {
        try {
            return new ResponseEntity<>(activityFormatService.getAllActivityFormat(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityFormat> getActivityFormatById(@PathVariable(name = "id") Long id) {
        try {
            Optional<ActivityFormat> activityFormat = Optional.ofNullable(activityFormatService.getActivityFormatById(id));

            if (activityFormat.isPresent()) {
                return new ResponseEntity<>(activityFormat.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<ActivityFormat> createActivityFormat(@RequestBody ActivityFormatCreateDto activityFormatCreateDto){
        try{
            return new ResponseEntity<>(activityFormatService.createActivityFormat(activityFormatCreateDto), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ActivityFormat> updateActivityFormat(@PathVariable(name = "id") Long id, @RequestBody ActivityFormatCreateDto activityFormatCreateDto){
        try {
            return new ResponseEntity<>(activityFormatService.updateActivityFormat(id, activityFormatCreateDto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ActivityFormat> deleteActivityFormat(@PathVariable(name = "id") Long id){
        try {
            activityFormatService.deleteActivityFormat(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
