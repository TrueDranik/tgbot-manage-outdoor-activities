package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.ActivityEnum;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.model.common.ActivityEnum.SCHEDULE_ACTIVITY;

@RequiredArgsConstructor
@Service
public class CallbackScheduleActivityImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(SCHEDULE_ACTIVITY);
    private final ScheduleRepository scheduleRepository;
    private final ActivityRepository activityRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String scheduleId = callbackQuery.getData().split("/")[1];

        List<Schedule> schedules = scheduleRepository.findAll();
        Optional<Activity> activity = activityRepository.findById(Long.parseLong(scheduleId));

        if (generateKeyboardWithSchedule(schedules, activity).getKeyboard().size() <= 1) {
            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(chatId)
                    .text("❌Расписание для '" + activity.get().getName() + "' отсутствует.\nВернитесь назад.")
                    .replyMarkup(generateKeyboardWithSchedule(schedules, activity))
                    .build();
        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Выберите дату для '" + activity.get().getName() + "'")
                .replyMarkup(generateKeyboardWithSchedule(schedules, activity))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithSchedule(List<Schedule> schedules, Optional<Activity> activity) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();
        schedules.forEach(i -> {
            if (activity.get().getName().equals(i.getActivityId().getName())) {
                rowMain.add(InlineKeyboardButton.builder()
                        .text(i.getEventDate().toString())
                        .callbackData("SCHEDULE_OPTION/" + i.getId())
                        .build());
                if (rowMain.size() == 2) {
                    List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                    mainKeyboard.add(temporaryKeyboardRow);
                    rowMain.clear();
                }
            }
        });

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text("⬅️Назад")
                .callbackData("SCHEDULE")
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
