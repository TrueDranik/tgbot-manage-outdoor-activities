package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "selected_schedule")
public class SelectedSchedule {
    @Id
    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "schedule_id")
    private Long scheduleId;
}
