package com.bot.sup.mapper;

import com.bot.sup.model.dto.InstructorDto;
import com.bot.sup.model.entity.Instructor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstructorMapper {
    InstructorDto toEntityDTO(Instructor instructor);

    Instructor DTOtoEntity(InstructorDto instructorDto);
}
