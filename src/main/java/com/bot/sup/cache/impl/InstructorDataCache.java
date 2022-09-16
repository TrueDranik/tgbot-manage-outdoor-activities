package com.bot.sup.cache.impl;

import com.bot.sup.cache.DataCache;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.model.dto.InstructorDto;
import com.bot.sup.model.entity.Instructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InstructorDataCache implements DataCache {
    private final Map<Long, RegistrationInstructorStateEnum> instructorState = new ConcurrentHashMap<>();
    private final Map<Long, Instructor> instructorData = new ConcurrentHashMap<>();

    private final Map<Long, Long> instructorForUpdate = new ConcurrentHashMap<>();


    @Override
    public void setInstructorCurrentState(Long instructorId, RegistrationInstructorStateEnum instructorRegistrutionState) {
        instructorState.put(instructorId, instructorRegistrutionState);
    }

    @Override
    public RegistrationInstructorStateEnum getInstructorCurrentState(Long instructorId) {
        return instructorState.getOrDefault(instructorId, RegistrationInstructorStateEnum.FILLING_PROFILE);
    }

    public Instructor getInstructorProfileData(Long userId) {
        return instructorData.getOrDefault(userId, new Instructor());
    }

    public void saveInstructorProfileData(Long userId, Instructor instructorDto) {
        instructorData.put(userId, instructorDto);
    }

    public void saveInstructorForUpdate(Long chatId, Long instructorId) {
        instructorForUpdate.put(chatId, instructorId);
    }

    public void removeInstructorForUpdate(Long chatId) {
        instructorForUpdate.remove(chatId);
    }

    public Long getInstructorForUpdate(Long chatId) {
        return instructorForUpdate.get(chatId);
    }
}
