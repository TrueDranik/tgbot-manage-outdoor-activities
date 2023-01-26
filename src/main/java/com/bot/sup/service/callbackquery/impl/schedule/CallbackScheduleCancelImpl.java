package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.schedule.impl.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackScheduleCancelImpl implements Callback {
    private final ScheduleServiceImpl scheduleService;
    private final ScheduleMessageProperties scheduleMessageProperties;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        Schedule schedule = scheduleService.findScheduleById(Long.valueOf(scheduleId));

        return SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text(String.format(scheduleMessageProperties.getConfirmCancelEvent(), schedule.getActivity().getName()))
                .parseMode(ParseMode.MARKDOWN)
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
                .callbackData(CallbackEnum.SCHEDULE_INFO + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.SCHEDULE_CANCEL;
    }
}
