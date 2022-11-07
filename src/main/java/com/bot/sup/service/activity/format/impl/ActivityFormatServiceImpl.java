package com.bot.sup.service.activity.format.impl;

import com.bot.sup.mapper.ActivityFormatMapper;
import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.activity.format.ActivityFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityFormatServiceImpl implements ActivityFormatService {
    private final ActivityFormatRepository activityFormatRepository;
    private final ActivityFormatMapper activityFormatMapper;

    public void save(ActivityFormat activityFormat) {
        activityFormatRepository.save(activityFormat);
    }

    @Override
    public List<ActivityFormat> getAllActivityFormat() {
        List<ActivityFormat> activityFormats = new ArrayList<>(activityFormatRepository.findAll());

        if (activityFormats.isEmpty()) {
            throw new EntityNotFoundException("Activity format not found");
        }

        return activityFormats;
    }

    @Override
    public ActivityFormat getActivityFormatById(Long id) {
        return activityFormatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity format with id[" + id + "] not found"));
    }

    @Override
    public ActivityFormat createActivityFormat(ActivityFormatCreateDto activityFormatCreateDto) {
        ActivityFormat activityFormat = activityFormatMapper.dtoToDomain(activityFormatCreateDto);

        return activityFormatRepository.save(activityFormat);
    }

    @Override
    public ActivityFormat updateActivityFormat(Long id, ActivityFormatCreateDto activityFormatCreateDto) {
        ActivityFormat activityFormat = activityFormatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity format with id[" + id + "] not found"));

        activityFormat.setName(activityFormatCreateDto.getName());

        return activityFormatRepository.save(activityFormat);
    }

    @Override
    public void deleteActivityFormat(Long id) {
        ActivityFormat activityFormat = activityFormatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity format with id[" + id + "] not found"));

        activityFormatRepository.delete(activityFormat);
    }
}
