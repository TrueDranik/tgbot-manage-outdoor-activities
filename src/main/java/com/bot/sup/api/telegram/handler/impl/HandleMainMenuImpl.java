package com.bot.sup.api.telegram.handler.impl;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.SelectedSchedule;
import com.bot.sup.repository.SelectedScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HandleMainMenuImpl implements Handle {
    private final SelectedScheduleRepository selectedScheduleRepository;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;

    @Transactional
    @Override
    public BotApiMethod<?> getMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        Optional<SelectedSchedule> selectedActivity = selectedScheduleRepository.findByTelegramId(chatId);
        if (selectedActivity.isPresent()) {
            selectedScheduleRepository.deleteByTelegramId(chatId);
        }

        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(mainMessageProperties.getUserChoose())
                .replyMarkup(createInlineKeyboard())
                .build();
    }

    public InlineKeyboardMarkup createInlineKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getSchedules())
                .callbackData(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(instructorMessageProperties.getInstructors())
                .callbackData(CallbackEnum.INSTRUCTORS.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(activityMessageProperties.getActivities())
                .callbackData(CallbackEnum.SUP_ACTIVITIES.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getTourEditor())
                .callbackData(CallbackEnum.SCHEDULE_WEBAPP.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("\uD83C\uDF1F О нас!")
                .callbackData(CallbackEnum.ABOUT_US_INFO.toString())
                .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }
}
