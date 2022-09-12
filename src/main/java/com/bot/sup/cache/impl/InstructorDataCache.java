package com.bot.sup.cache.impl;

import com.bot.sup.cache.DataCache;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.model.dto.InstructorDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InstructorDataCache implements DataCache {
    private final Map<Long, RegistrationInstructorStateEnum> instructorState = new ConcurrentHashMap<>();
    private final Map<Long, InstructorDto> instructorData = new ConcurrentHashMap<>();


    @Override
    public void setInstructorCurrentState(Long instructorId, RegistrationInstructorStateEnum instructorRegistrutionState) {
        instructorState.put(instructorId, instructorRegistrutionState);
    }

    @Override
    public RegistrationInstructorStateEnum getInstructorCurrentState(Long instructorId) {
        return instructorState.getOrDefault(instructorId, RegistrationInstructorStateEnum.FILLING_PROFILE);
    }

    public InstructorDto getInstructorProfileData(Long userId) {
        return instructorData.getOrDefault(userId, new InstructorDto());
    }

    public void saveInstructorProfileData(Long userId, InstructorDto instructorDto) {
        instructorData.put(userId, instructorDto);
    }
}
