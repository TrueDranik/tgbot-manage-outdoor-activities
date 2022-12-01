package com.bot.sup.api.telegram.handler.impl;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.*;
import com.bot.sup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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

    public BotApiMethod<?> getMessage(Update update) {
        val callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        Schedule schedule = scheduleRepository.findById(Long.parseLong(scheduleId))
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id [" + scheduleId + "] not found"));

        Optional<Activity> optionalActivity = Optional.ofNullable(schedule.getActivity());
        Optional<Route> optionalRoute = Optional.ofNullable(schedule.getRoute());

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Дата и время старта: " + schedule.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        + " " + LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " ("
                        + schedule.getEventDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))
                        + ")\n"
                        + "Формат активности: " + optionalActivity.map(Activity::getActivityFormat)
                        .filter(format -> format.getName() != null).map(ActivityFormat::getName).orElse("Не найдено!") + "\n"
                        + "Тип активности: " + optionalActivity.map(Activity::getActivityType)
                        .filter(type -> type.getName() != null).map(ActivityType::getName).orElse("Не найдено!") + "\n"
                        + "Название маршрута: " +optionalRoute.map(Route::getName).orElse("Не найдено!") + "\n"
                        + "Точка старта: " + optionalRoute.map(Route::getStartPointName).orElse("Не найдено!") + "\n"
                        + "Координаты старта: " + optionalRoute.map(Route::getStartPointCoordinates).orElse("Не найдено!"))
                .parseMode("Markdown")
                .replyMarkup(createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                .build();
    }

    public InlineKeyboardMarkup createInlineKeyboard(String activityFormatId, String eventDate, String scheduleId) {
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
                .text("Изменить (в работе)")
                .webApp(new WebAppInfo("https://tgsupbot-admin.reliab.tech/"))
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
}
