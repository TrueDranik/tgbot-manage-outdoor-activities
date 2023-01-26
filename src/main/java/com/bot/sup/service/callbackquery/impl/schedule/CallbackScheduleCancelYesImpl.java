package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.schedule.impl.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackScheduleCancelYesImpl implements Callback {
    private final ScheduleServiceImpl scheduleService;
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;

    @Transactional
    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String scheduleId = callbackQuery.getData().split("/")[1];

        Schedule schedule = scheduleService.findScheduleById(Long.valueOf(scheduleId));
        scheduleService.deleteSchedule(Long.valueOf(scheduleId));

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text(String.format(scheduleMessageProperties.getEventCancelled(), schedule.getActivity().getName()))
                .replyMarkup(createInlineKeyboard())
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard() {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString())
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.SCHEDULE_CANCEL_YES;
    }
}
