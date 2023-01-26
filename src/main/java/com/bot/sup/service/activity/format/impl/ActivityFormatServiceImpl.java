package com.bot.sup.service.activity.format.impl;

import com.bot.sup.mapper.ActivityFormatMapper;
import com.bot.sup.model.dto.ActivityFormatDto;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.ActivityFormat_;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.activity.format.ActivityFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    public boolean existsByNameEqualsIgnoreCase(String userAnswer) {
        return activityFormatRepository.existsByNameEqualsIgnoreCase(userAnswer);
    }

    @Override
    public List<ActivityFormatDto> getAllActivityFormat() {
        List<ActivityFormat> activityFormats = activityFormatRepository.findAll();

        return activityFormatMapper.domainsToDtos(activityFormats);
    }

    @Override
    public ActivityFormatDto getActivityFormatById(Long id) {
        ActivityFormat activityFormatById = findActivityFormatById(id);

        return activityFormatMapper.domainToDto(activityFormatById);
    }

    @Override
    public ActivityFormatDto createActivityFormat(ActivityFormatDto activityFormatDto) {
        ActivityFormat activityFormat = activityFormatMapper.dtoToDomain(activityFormatDto);
        activityFormatRepository.save(activityFormat);

        return activityFormatMapper.domainToDto(activityFormat);
    }

    @Override
    public ActivityFormatDto updateActivityFormat(Long id, ActivityFormatDto activityFormatDto) {
        ActivityFormat activityToUpdate = activityFormatMapper.dtoToDomain(activityFormatDto);
        ActivityFormat activityFromDb = findActivityFormatById(id);

        BeanUtils.copyProperties(activityToUpdate, activityFromDb, ActivityFormat_.ID);
        activityFormatRepository.save(activityFromDb);

        return activityFormatMapper.domainToDto(activityFromDb);
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
}
