package com.bot.sup.cache;

import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.model.dto.InstructorDto;

public interface DataCache {
    void setInstructorCurrentState(Long instructorId, RegistrationInstructorStateEnum instructorRegistrutionState);

    RegistrationInstructorStateEnum getInstructorCurrentState(Long instructorId);

    InstructorDto getInstructorProfileData(Long instructorId);

    void saveInstructorProfileData(Long instructorId, InstructorDto instructorDto);
}
