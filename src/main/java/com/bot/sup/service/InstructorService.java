package com.bot.sup.service;

import com.bot.sup.mapper.InstructorMapper;
import com.bot.sup.model.dto.InstructorDto;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    public void save(InstructorDto instructorDto) {
        Instructor instructor = instructorMapper.DTOtoEntity(instructorDto);
        instructorRepository.save(instructor);
    }
}
