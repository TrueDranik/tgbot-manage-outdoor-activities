package com.bot.sup.service.activity.format.impl;

import com.bot.sup.mapper.ActivityFormatMapper;
import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.dto.ActivityFormatDto;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.activity.format.ActivityFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityFormatServiceImpl implements ActivityFormatService {
    private final ActivityFormatRepository activityFormatRepository;
    private final ActivityFormatMapper activityFormatMapper;

    public void save(ActivityFormat activityFormat) {
        activityFormatRepository.save(activityFormat);
    }

    public boolean existsByNameEqualsIgnoreCase(String userAnswer){
        return activityFormatRepository.existsByNameEqualsIgnoreCase(userAnswer);
    }

    @Override
    public List<ActivityFormatDto> getAllActivityFormat() {
        List<ActivityFormat> activityFormats = activityFormatRepository.findAll();
        return activityFormatMapper.ActivityFormatToDto(activityFormats);
    }

    @Override
    public ActivityFormatDto getActivityFormatById(Long id) {
        ActivityFormat activityFormatById = findActivityFormatById(id);

        return activityFormatToDto(activityFormatById);
    }

    @Override
    public ActivityFormatDto createActivityFormat(ActivityFormatCreateDto activityFormatCreateDto) {
        ActivityFormat activityFormat = new ActivityFormat();

        activityFormat.setName(activityFormatCreateDto.getName());
        activityFormat.setDescription(activityFormat.getDescription());

        activityFormatRepository.save(activityFormat);

        return activityFormatToDto(activityFormat);
    }

    @Override
    public ActivityFormatDto updateActivityFormat(Long id, ActivityFormatCreateDto activityFormatCreateDto) {
        ActivityFormat activityFormat = findActivityFormatById(id);

        dtoToActivityFormat(activityFormatCreateDto, activityFormat);

        activityFormatRepository.save(activityFormat);

        return activityFormatToDto(activityFormat);
    }

    @Override
    public void deleteActivityFormat(Long id) {
        ActivityFormat activityFormat = findActivityFormatById(id);

        activityFormatRepository.delete(activityFormat);
    }

    public ActivityFormat findActivityFormatById(Long id) {
        return activityFormatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity format with id[" + id + "] not found"));
    }

    private static ActivityFormatDto activityFormatToDto(ActivityFormat activityFormatById) {
        ActivityFormatDto activityFormatDto = new ActivityFormatDto();

        activityFormatDto.setId(activityFormatById.getId());
        activityFormatDto.setName(activityFormatById.getName());
        activityFormatDto.setDescription(activityFormatById.getDescription());

        return activityFormatDto;
    }

    private static void dtoToActivityFormat(ActivityFormatCreateDto activityFormatCreateDto, ActivityFormat activityFormat) {
        activityFormat.setName(activityFormatCreateDto.getName());
        activityFormat.setDescription(activityFormatCreateDto.getDescription());
    }
}
