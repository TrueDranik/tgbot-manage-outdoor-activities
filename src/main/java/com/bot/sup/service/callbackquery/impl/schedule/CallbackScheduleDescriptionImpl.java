package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackScheduleDescriptionImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleRepository scheduleRepository;

    private static final CallbackEnum ACTIVITIES = CallbackEnum.SCHEDULE_DESCRIPTION;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];
        if (callbackQuery.getMessage().hasPhoto()) {
            return SendMessage.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(description(scheduleId))
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                    .build();
        }
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text(description(scheduleId))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(String activityFormatId, String eventDate, String scheduleId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SCHEDULE_INFO + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .build();
    }

    private String description(String scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(Long.parseLong(scheduleId));
        Activity activity = schedule.get().getActivity();
        Route route = schedule.get().getRoute();

        String NOT_FOUND = "Не найдено!";

        List<String> descriptions = new ArrayList<>();
        descriptions.add("Дата и время старта: "
                + schedule.get().getEventTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " "
                + schedule.get().getEventDate().format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " ("
                + schedule.get().getEventDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))
                + ")");
        descriptions.add("Имя активности: " + activity.getName());
        descriptions.add("Сезонность: " + activity.getSeasonality());
        descriptions.add("Формат активности: " + activity.getActivityFormat().getName());
        descriptions.add("Тип активности: " + schedule.map(Schedule::getActivity)
                .filter(a -> a.getActivityType() != null)
                .map(Activity::getActivityType)
                .filter(at -> at.getName() != null)
                .map(ActivityType::getName).orElse(NOT_FOUND));
        descriptions.add("Описание: " + activity.getDescription());
        descriptions.add("Название маршрута: " + route.getName());
        descriptions.add("Точка старта: " + schedule.map(Schedule::getRoute)
                .filter(r -> r.getStartPointName() != null).map(Route::getStartPointName).orElse(NOT_FOUND));
        descriptions.add("Точка финиша: " + schedule.map(Schedule::getRoute)
                .filter(r -> r.getFinishPointName() != null).map(Route::getFinishPointName).orElse(NOT_FOUND));
        descriptions.add("Ссылка на карту: " + route.getMapLink());
        descriptions.add("Длина маршрута: " + route.getLength());
        descriptions.add("Продолжительность: " + activity.getDuration());
        descriptions.add("Возрастное ограничение: " + schedule.map(Schedule::getActivity)
                .filter(a -> a.getAge() != null).map(Activity::getName).orElse(NOT_FOUND));
        descriptions.add("Сложность: " + activity.getComplexity());
        descriptions.add("Стоимость: " + activity.getPrice());
        descriptions.add("Количество мест: " + schedule.map(Schedule::getParticipants).orElse(0));

        return String.join("\n", descriptions);
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
