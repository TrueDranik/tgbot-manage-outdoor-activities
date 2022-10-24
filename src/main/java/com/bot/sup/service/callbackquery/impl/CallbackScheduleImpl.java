package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import com.bot.sup.model.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bot.sup.model.common.CallbackEnum.SCHEDULE;

@RequiredArgsConstructor
@Service
public class CallbackScheduleImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final RouteRepository routeRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(SCHEDULE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<Route> activities = routeRepository.findAll();
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(scheduleMessageProperties.getSchedules())
                .replyMarkup(generateKeyboardWithActivity(activities))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<Route> activities) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();
//
//        activities.forEach(i -> {
//                    rowMain.add(InlineKeyboardButton.builder()
//                            .text(i.getName())
//                            .callbackData("SCHEDULE_ACTIVITY/" + i.getId())
//                            .build());
//                    if (rowMain.size() == 2) {
//                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
//                        mainKeyboard.add(temporaryKeyboardRow);
//                        rowMain.clear();
//                    }
//                }
//        );
//
//        if (rowMain.size() == 1) {
//            mainKeyboard.add(rowMain);
//        }
//
        WebAppInfo webAppInfo = WebAppInfo.builder()
                .url("https://192.168.1.35:3000")
                        .build();
        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getMenu())
                .webApp(webAppInfo)
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
