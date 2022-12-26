package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.model.entity.Instructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public interface InstructorRegistration {
    BotApiMethod<?> processInputMessage(Message message, Instructor instructor);
    boolean support(InstructorStateEnum instructorCurrentState);
}
