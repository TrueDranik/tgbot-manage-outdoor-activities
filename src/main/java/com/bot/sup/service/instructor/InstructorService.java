package com.bot.sup.service.instructor;

import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public void save(Instructor instructor) {
        instructorRepository.save(instructor);
    }

    public Instructor findByTelegramId(Long instructorId) {
        return instructorRepository.findByTelegramId(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("Instructor with telegramId [%s] not found".formatted(instructorId)));
    }

    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    public boolean existsByTelegramId(Long telegramId) {
        return instructorRepository.existsByTelegramId(telegramId);
    }

    @Transactional
    public void deleteInstructor(Long instructorId) {
        instructorRepository.deleteByTelegramId(instructorId);
    }
}
