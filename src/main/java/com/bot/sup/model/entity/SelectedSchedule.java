package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class SelectedSchedule {
    @Id
    private Long telegramId;
    private Long currentScheduleId;
}
