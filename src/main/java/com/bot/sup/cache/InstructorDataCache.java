package com.bot.sup.cache;

import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.model.entity.Instructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class InstructorDataCache {
    private final Map<Long, InstructorStateEnum> instructorState = new WeakHashMap<>();
    private final Map<Long, Instructor> instructorData = new WeakHashMap<>();
    private final Map<Long, Long> instructorForUpdate = new WeakHashMap<>();

    public void setInstructorCurrentState(Long chatId, InstructorStateEnum registrationState) {
        instructorState.put(chatId, registrationState);
    }

    public InstructorStateEnum getInstructorCurrentState(Long chatId) {
        return instructorState.getOrDefault(chatId, InstructorStateEnum.FILLING_INSTRUCTOR);
    }

    public void removeInstructorCurrentState(Long chatId){
        instructorState.remove(chatId);
    }

    public Instructor getInstructorProfileData(Long chatId) {
        return instructorData.getOrDefault(chatId, new Instructor());
    }

    public void saveInstructorProfileData(Long chatId, Instructor instructor) {
        instructorData.put(chatId, instructor);
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
