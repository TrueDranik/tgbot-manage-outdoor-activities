package com.bot.sup.api.telegram.handler.impl;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.TelegramProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.*;
import com.bot.sup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HandleScheduleInfoImpl {
    private final ScheduleRepository scheduleRepository;
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final TelegramProperties telegramProperties;

    public BotApiMethod<?> getMessage(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        Schedule schedule = scheduleRepository.findById(Long.parseLong(scheduleId))
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id [" + scheduleId + "] not found"));

        Optional<Activity> optionalActivity = Optional.ofNullable(schedule.getActivity());
        Optional<Route> optionalRoute = Optional.ofNullable(schedule.getRoute());

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text(scheduleInfo(eventDate, schedule, optionalActivity, optionalRoute))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                .build();
    }

    public static String scheduleInfo(String eventDate, Schedule schedule, Optional<Activity> optionalActivity, Optional<Route> optionalRoute) {
        String NOT_FOUND = "Не найдено!";
        return "Дата и время старта: " + schedule.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                + " " + LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " ("
                + schedule.getEventDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))
                + ")\n"
                + "Формат активности: " + optionalActivity.map(Activity::getActivityFormat)
                .filter(format -> format.getName() != null).map(ActivityFormat::getName).orElse(NOT_FOUND) + "\n"
                + "Тип активности: " + optionalActivity.map(Activity::getActivityType)
                .filter(type -> type.getName() != null).map(ActivityType::getName).orElse(NOT_FOUND) + "\n"
                + "Название маршрута: " + optionalRoute.map(Route::getName).orElse(NOT_FOUND) + "\n"
                + "Точка старта: " + optionalRoute.map(Route::getStartPointName).orElse(NOT_FOUND) + "\n"
                + "Координаты старта: " + optionalRoute.map(Route::getStartPointCoordinates).orElse(NOT_FOUND);
    }

    public InlineKeyboardMarkup createInlineKeyboard(String activityFormatId, String eventDate, String scheduleId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getClientInformation())
                .webApp(new WebAppInfo(telegramProperties.getWebAppClientRecords()))
//                .callbackData(CallbackEnum.SCHEDULE_ALL_CLIENT_INFO + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());
        firstRow.add(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getFullDescription())
                .callbackData(CallbackEnum.SCHEDULE_DESCRIPTION + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());

        secondRow.add(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getChangeSchedule())
                .webApp(new WebAppInfo(telegramProperties.getUpdateSchedule()))
                .build());
        secondRow.add(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getCancelSchedule())
                .callbackData(CallbackEnum.SCHEDULE_CANCEL + "/" + scheduleId)
                .build());

        thirdRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.DATE_TO_ROUTE + "/" + activityFormatId + "/" + eventDate)
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .keyboardRow(thirdRow)
                .build();
    }
}
