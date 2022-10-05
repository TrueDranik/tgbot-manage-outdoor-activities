package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bot.sup.model.common.CallbackEnum.SCHEDULE;

@RequiredArgsConstructor
@Service
public class CallbackScheduleImpl implements Callback {
    public static final Set<CallbackEnum> ACTIVITIES = Set.of(SCHEDULE);
    private final ActivityRepository activityRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<Activity> activities = activityRepository.findAll();
        Long chatId = callbackQuery.getMessage()
                .getChatId();
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Расписание какой активности Вы хотите настроить?")
                .replyMarkup(generateKeyboardWithActivity(activities))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<Activity> activities) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        activities.forEach(i -> {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getName())
                            .callbackData("SCHEDULE_ACTIVITY/" + i.getId())
                            .build());
                    if (rowMain.size() == 2) {
                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                        mainKeyboard.add(temporaryKeyboardRow);
                        rowMain.clear();
                    }
                }
        );

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text("↖️Меню")
                .callbackData("MENU")
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
