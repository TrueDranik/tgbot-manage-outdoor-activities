package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.MenuMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.repository.RouteRepository;
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

import static com.bot.sup.model.common.CallbackEnum.SCHEDULE_ACTIVITY;

@RequiredArgsConstructor
@Service
public class CallbackScheduleActivityImpl implements Callback {
    private final MenuMessageProperties menuMessageProperties;
    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;
    private final ActivityFormatRepository activityFormatRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(SCHEDULE_ACTIVITY);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String scheduleId = callbackQuery.getData().split("/")[1];

        List<Schedule> schedules = scheduleRepository.findAll();
        Optional<ActivityFormat> activityFormat = activityFormatRepository.findById(Long.parseLong(scheduleId));

        if (generateKeyboardWithSchedule(schedules, activityFormat).getKeyboard().size() <= 1) {
            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(chatId)
                    .text("❌ Расписание для '" + activityFormat.get().getName() + "' отсутствует.\nВернитесь назад.")
                    .replyMarkup(generateKeyboardWithSchedule(schedules, activityFormat))
                    .build();
        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Выберите дату для '" + activityFormat.get().getName() + "'")
                .replyMarkup(generateKeyboardWithSchedule(schedules, activityFormat))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithSchedule(List<Schedule> schedules, Optional<ActivityFormat> activityFormat) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        schedules.forEach(i -> {
            if (activityFormat.get().getName().equals(i.getName())) {
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
                .text(menuMessageProperties.getBack())
                .callbackData("SCHEDULE")
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
