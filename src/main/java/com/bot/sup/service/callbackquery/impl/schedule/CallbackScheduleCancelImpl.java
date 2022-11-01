package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CallbackScheduleCancelImpl implements Callback {
    private final ScheduleRepository scheduleRepository;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.SCHEDULE_CANCEL);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        Optional<Schedule> schedule = scheduleRepository.findById(Long.parseLong(scheduleId));

        return SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("Вы уверены, что хотите отменить собитыие *" + schedule.get().getActivity().getName() + "*?")
                .parseMode("Markdown")
                .replyMarkup(createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(String activityFormatId, String eventDate, String scheduleId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text("Да")
                .callbackData(CallbackEnum.SCHEDULE_CANCEL_YES + "/" + scheduleId)
                .build());
        firstRow.add(InlineKeyboardButton.builder()
                .text("Нет")
                .callbackData(CallbackEnum.SCHEDULE_CANCEL_NO.toString())
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
