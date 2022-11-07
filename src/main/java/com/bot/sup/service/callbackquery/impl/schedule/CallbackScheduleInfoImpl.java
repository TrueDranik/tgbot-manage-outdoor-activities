package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CallbackScheduleInfoImpl implements Callback {
    private final ScheduleRepository scheduleRepository;
    private final MainMessageProperties mainMessageProperties;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.SCHEDULE_INFO);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        Optional<Schedule> schedule = scheduleRepository.findById(Long.parseLong(scheduleId));

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Дата и время старта: " + schedule.get().getEventTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        + " " + LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " ("
                        + schedule.get().getEventDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))
                        + ")\n"
                        + "Формат активности: " + schedule.get().getActivity().getActivityFormat().getName() + "\n"
                        + "Тип активности: " + schedule.get().getActivity().getActivityType().getName() + "\n"
                        + "Название маршрута: " + schedule.get().getActivity().getRoute().getName() + "\n"
                        + "Точка старта: " + schedule.get().getActivity().getRoute().getStartPointName())
                .parseMode("Markdown")
                .replyMarkup(createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(String activityFormatId, String eventDate, String scheduleId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text("Информация о клиентах")
                .callbackData(CallbackEnum.SCHEDULE_ALL_CLIENT_INFO + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());
        firstRow.add(InlineKeyboardButton.builder()
                .text("Полное описание")
                .callbackData(CallbackEnum.SCHEDULE_DESCRIPTION + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());

        secondRow.add(InlineKeyboardButton.builder()
                .text("Изменить")
                .webApp(new WebAppInfo("https://telegram.org/"))
                .build());
        secondRow.add(InlineKeyboardButton.builder()
                .text("Отменить")
                .callbackData(CallbackEnum.SCHEDULE_CANCEL + "/" + scheduleId)
                .build());

        thirdRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.DATE_TO_ROUTE + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .keyboardRow(thirdRow)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
