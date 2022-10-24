package com.bot.sup.service.activityFormat;

import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.entity.ActivityFormat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityFormatService {
    List<ActivityFormat> getAllActivityFormat();

    ActivityFormat getActivityFormatById(Long id);

    ActivityFormat createActivityFormat(ActivityFormatCreateDto activityFormatCreateDto);

    ActivityFormat updateActivityFormat(Long id, ActivityFormatCreateDto activityFormatCreateDto);

    void deleteActivityFormat(Long id);
}
