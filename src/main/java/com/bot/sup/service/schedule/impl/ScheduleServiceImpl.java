package com.bot.sup.service.schedule.impl;

import com.bot.sup.mapper.ScheduleCreateMapper;
import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.model.ScheduleParams;
import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.dto.ScheduleDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.model.entity.Schedule_;
import com.bot.sup.model.entity.SelectedSchedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.SelectedScheduleRepository;
import com.bot.sup.repository.specification.ScheduleFilterByDateAndTimeForDaySpecification;
import com.bot.sup.repository.specification.ScheduleFilterByDateSpecification;
import com.bot.sup.repository.specification.ScheduleSpecification;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final SelectedScheduleRepository selectedScheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleCreateMapper scheduleCreateMapper;

    public void saveSelectedSchedule(SelectedSchedule selectedSchedule) {
        selectedScheduleRepository.save(selectedSchedule);
    }

    public void deleteSelectedScheduleByTelegramId(Long telegramId) {
        selectedScheduleRepository.deleteByTelegramId(telegramId);
    }

    public List<Schedule> getSchedulesByActivityFormatId(Long activityFormatId) {
        return scheduleRepository.getSchedulesByActivity_ActivityFormat_Id(activityFormatId);
    }

    public List<Schedule> findScheduleByActivityFormatIdAndEventDate(Long activityFormatId, LocalDate eventDate) {
        return scheduleRepository
                .selectScheduleByActivityFormatIdAndEventDate(activityFormatId, eventDate);
    }

    public SelectedSchedule findSelectedScheduleByTelegramId(Long telegramId) {
        return selectedScheduleRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User with Telegram id[" + telegramId + "] didn't selected schedule"));
    }

    @Override
    public ScheduleDto getScheduleByTelegramId(Long telegramId) {
        Optional<SelectedSchedule> byTelegramId = Optional.ofNullable(findSelectedScheduleByTelegramId(telegramId));

        Schedule schedule = null;
        if (byTelegramId.isPresent()) {
            Long scheduleId = byTelegramId.get().getCurrentScheduleId();

            schedule = findScheduleById(scheduleId);
        }

        return scheduleMapper.domainToDto(schedule);
    }

    @Override
    public List<ScheduleDto> getAllSchedule(ScheduleRequestParams params) {
        return scheduleRepository.findAll(new ScheduleSpecification(params));
    }

    @Override
    public ScheduleDto getScheduleById(Long id) {
        Schedule scheduleById = findScheduleById(id);

        return scheduleMapper.domainToDto(scheduleById);
    }

    @Transactional
    @Override
    public List<ScheduleCreateDto> createSchedule(List<ScheduleCreateDto> scheduleCreateDto) {
        if (scheduleCreateDto == null) {
            return Collections.emptyList();
        }

        List<Schedule> schedules = scheduleCreateMapper.dtosToDomains(scheduleCreateDto);
        scheduleRepository.saveAll(schedules);

        return scheduleCreateMapper.domainsToDtos(schedules);
    }

    @Override
    public ScheduleCreateDto updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto) {
        Schedule scheduleForUpdate = scheduleCreateMapper.dtoToDomain(scheduleCreateDto);
        Schedule scheduleFromDb = findScheduleById(id);

        BeanUtils.copyProperties(scheduleForUpdate, scheduleFromDb, Schedule_.ID, Schedule_.IS_ACTIVE);
        scheduleRepository.save(scheduleFromDb);

        return scheduleCreateMapper.domainToDto(scheduleFromDb);
    }

    @Override
    public List<ScheduleDto> getAllFilteredSchedule(ScheduleParams scheduleParams) {
        return scheduleRepository.findAll(new ScheduleFilterByDateSpecification(scheduleParams));
    }

    @Override
    public List<ScheduleDto> getAllFilteredSchedule(Boolean isActive, LocalDate eventDate, LocalTime eventTime) {
        ScheduleParams scheduleParams = new ScheduleParams(isActive, eventDate, eventTime);
        List<ScheduleDto> schedules = getFilteredByTimeSchedulesForCurrentDate(scheduleParams);
        schedules.addAll(getFilteredSchedulesFromNextDay(scheduleParams));

        return schedules;
    }

    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        Schedule scheduleById = findScheduleById(id);
        scheduleById.setIsActive(false);

        scheduleRepository.save(scheduleById);
    }

    private List<ScheduleDto> getFilteredByTimeSchedulesForCurrentDate(ScheduleParams scheduleParams) {
        return scheduleRepository.findAll(new ScheduleFilterByDateAndTimeForDaySpecification(scheduleParams));
    }

    private List<ScheduleDto> getFilteredSchedulesFromNextDay(ScheduleParams scheduleParams) {
        scheduleParams.setEventDate(scheduleParams.getEventDate().plusDays(1));
        return scheduleRepository.findAll(new ScheduleFilterByDateSpecification(scheduleParams));

    }

    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id[" + id + "] not found"));
    }
}
