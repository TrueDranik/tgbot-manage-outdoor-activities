package com.bot.sup.service;

import com.bot.sup.model.dto.CreateTourDto;
import com.bot.sup.model.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface NewTourService {

    public void save(ScheduleDto scheduleDto);


}
