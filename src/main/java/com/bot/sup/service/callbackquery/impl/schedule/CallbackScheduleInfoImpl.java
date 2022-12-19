package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.api.telegram.handler.impl.HandleScheduleInfoImpl;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.ImageData;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ImageDataRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackScheduleInfoImpl implements Callback {
    private final ScheduleRepository scheduleRepository;
    private final ImageDataRepository imageDataRepository;
    private final HandleScheduleInfoImpl handleScheduleInfo;


    public static final CallbackEnum ACTIVITIES = CallbackEnum.SCHEDULE_INFO;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        Schedule schedule = scheduleRepository.findById(Long.parseLong(scheduleId))
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id [" + scheduleId + "] not found"));

        Optional<Activity> optionalActivity = Optional.ofNullable(schedule.getActivity());
        Optional<Route> optionalRoute = Optional.of(Optional.ofNullable(schedule.getRoute())
                .orElseThrow(() -> new EntityNotFoundException("Маршрут не найден")));

        Optional<ImageData> imageData = imageDataRepository.findByRouteId(optionalRoute.get().getId());

        InputStream photo;
        String imageName;
        if (imageData.isEmpty()) {
            photo = this.getClass().getClassLoader().getResourceAsStream("map.jpg");
            imageName = "map.jpg";
        } else {
            photo = new ByteArrayInputStream(imageData.get().getImageData());
            imageName = imageData.get().getName();
        }
        return SendPhoto.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .photo(new InputFile(photo, imageName))
                .caption(HandleScheduleInfoImpl.scheduleInfo(eventDate, schedule, optionalActivity, optionalRoute))
                .parseMode("Markdown")
                .replyMarkup(handleScheduleInfo.createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
