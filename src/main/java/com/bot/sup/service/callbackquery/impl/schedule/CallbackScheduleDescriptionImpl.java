package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Route;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CallbackScheduleDescriptionImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleRepository scheduleRepository;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.SCHEDULE_DESCRIPTION);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text(description(scheduleId))
                .parseMode("Markdown")
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
        Route route = schedule.get().getActivity().getRoute();
        String enter = "\n";

        String eventDateTime = "Дата и время старта: "
                + schedule.get().getEventTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " "
                + schedule.get().getEventDate().format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " ("
                + schedule.get().getEventDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))
                + ")" + enter;
        String name = "Имя активности: " + activity.getName() + enter;
        String seasonality = "Сезонность: " + activity.getSeasonality() + enter;
        String activityFormat = "Формат активности: " + activity.getActivityFormat().getName() + enter;
        String activityType = "Тип активности: " + activity.getActivityType().getName() + enter;
        String description = "Описание: " + activity.getDescription() + enter;
        String routeName = "Название маршрута: " + route.getName() + enter;
        String routeStartPointName = "Точка старта: " + route.getStartPointName() + enter;
        String routeFinishPointName = "Точка финиша: " + route.getFinishPointName() + enter;
        String routeLink = "Ссылка на карту: " + route.getMapLink() + enter;
        String routeLength = "Длина маршрута: " + route.getLength() + enter;
        String duration = "Продолжительность: " + activity.getDuration() + enter;
        String age = "Возрастное ограничение: " + activity.getAge() + enter;
        String complexity = "Сложность: " + activity.getComplexity() + enter;
        String price = "Стоимость: " + activity.getPrice() + enter;
        String participants = "Количество мест: " + schedule.get().getParticipants() + enter;

        return eventDateTime + name + seasonality + activityFormat + activityType + description + routeName + routeStartPointName
                + routeFinishPointName + routeLink + routeLength + duration + age + complexity + price + participants;
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
