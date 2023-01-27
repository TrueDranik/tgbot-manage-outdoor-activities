package com.bot.sup.service.activity.format;

import com.bot.sup.model.dto.ActivityFormatDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityFormatService {
    List<ActivityFormatDto> getAllActivityFormat();

    ActivityFormatDto getActivityFormatById(Long id);

    ActivityFormatDto createActivityFormat(ActivityFormatDto activityFormatCreateDto);

    ActivityFormatDto updateActivityFormat(Long id, ActivityFormatDto activityFormatCreateDto);

    void deleteActivityFormat(Long id);
}
