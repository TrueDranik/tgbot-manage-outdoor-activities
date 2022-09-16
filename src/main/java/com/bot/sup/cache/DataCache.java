package com.bot.sup.cache;

import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.model.entity.Instructor;

public interface DataCache {
    void setInstructorCurrentState(Long instructorId, RegistrationInstructorStateEnum instructorRegistrutionState);

    RegistrationInstructorStateEnum getInstructorCurrentState(Long instructorId);

    Instructor getInstructorProfileData(Long instructorId);

    void saveInstructorProfileData(Long instructorId, Instructor instructorDto);
}
