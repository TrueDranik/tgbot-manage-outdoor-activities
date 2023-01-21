package com.bot.sup.service;

import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import com.bot.sup.api.telegram.handler.impl.HandleScheduleInfoImpl;
import com.bot.sup.api.telegram.handler.impl.HandleScheduleMenuImpl;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.*;
import com.bot.sup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerateKeyboard {
    private final Bot bot;
    private final MainMessageProperties mainMessageProperties;
    private final HandleMainMenuImpl handleMainMenu;
    private final HandleScheduleMenuImpl handleScheduleMenu;
    private final HandleScheduleInfoImpl handleScheduleInfo;
    private final ScheduleRepository scheduleRepository;

    public Integer getMessageId(Long chatId) {
        Update update = bot.getUserUpdate();
        if (update.getCallbackQuery().getMessage().getChatId().equals(chatId)) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }

        return null;
    }

    public void mainMenu(Long chatId, Integer messageId) throws TelegramApiException {
        bot.execute(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text(mainMessageProperties.getUserChoose())
                        .replyMarkup(handleMainMenu.createInlineKeyboard())
                        .build());
    }

    public void scheduleMenu(Long chatId, Integer messageId) throws TelegramApiException {
        bot.execute(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text("Меню расписания")
                        .replyMarkup(handleScheduleMenu.createInlineKeyboard())
                        .build());
    }

    public void scheduleInfo(Long chatId, Integer messageId, String activityFormatId, String eventDate, String scheduleId) throws TelegramApiException {
        Schedule schedule = scheduleRepository.findById(Long.parseLong(scheduleId))
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id [" + scheduleId + "] not found"));

        Optional<Activity> optionalActivity = Optional.ofNullable(schedule.getActivity());
        Optional<Route> optionalRoute = Optional.ofNullable(schedule.getRoute());

        bot.execute(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text("Дата и время старта: " + schedule.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " " + LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " ("
                                + schedule.getEventDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))
                                + ")\n"
                                + "Формат активности: " + optionalActivity.map(Activity::getActivityFormat)
                                .filter(format -> format.getName() != null).map(ActivityFormat::getName).orElse("Не найдено!") + "\n"
                                + "Тип активности: " + optionalActivity.map(Activity::getActivityType)
                                .filter(type -> type.getName() != null).map(ActivityType::getName).orElse("Не найдено!") + "\n"
                                + "Название маршрута: " + optionalRoute.map(Route::getName).orElse("Не найдено!") + "\n"
                                + "Точка старта: " + optionalRoute.map(Route::getStartPointName).orElse("Не найдено!") + "\n"
                                + "Координаты старта: " + optionalRoute.map(Route::getStartPointCoordinates).orElse("Не найдено!"))
                        .parseMode(ParseMode.MARKDOWN)
                        .replyMarkup(handleScheduleInfo.createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                        .build());
    }
}
