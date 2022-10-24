package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Client;
import com.bot.sup.model.entity.Instructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ScheduleDto {
    private Long id;
    private Activity activity;
    private LocalDateTime eventDate;
    private Integer participants;
    private List<Instructor> instructor;
    private List<Client> client;
}
