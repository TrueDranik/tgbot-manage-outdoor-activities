package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.api.telegram.handler.impl.HandleScheduleInfoImpl;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.entity.*;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.files.ImageDataService;
import com.bot.sup.service.schedule.impl.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
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
    private final ImageDataService imageDataService;
    private final ScheduleServiceImpl scheduleService;
    private final HandleScheduleInfoImpl handleScheduleInfo;

    // TODO: 26.01.2023
    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        SelectedSchedule selectedSchedule = new SelectedSchedule();
        selectedSchedule.setTelegramId(callbackQuery.getMessage().getChatId());
        selectedSchedule.setCurrentScheduleId(Long.valueOf(scheduleId));
        scheduleService.saveSelectedSchedule(selectedSchedule);

        Schedule schedule = scheduleService.findScheduleById(Long.valueOf(scheduleId));

        Optional<Activity> optionalActivity = Optional.ofNullable(schedule.getActivity());
        Optional<Route> optionalRoute = Optional.of(Optional.ofNullable(schedule.getRoute())
                .orElseThrow(() -> new EntityNotFoundException("Маршрут не найден")));

        Optional<ImageData> imageData = Optional.ofNullable(imageDataService.findByRouteId(optionalRoute.get().getId()));

        InputStream photo;
        String imageName;
        if (imageData.isEmpty()) {
            photo = this.getClass().getClassLoader().getResourceAsStream("map.jpg");
            imageName = "map.jpg";
        } else {
            photo = new ByteArrayInputStream(imageData.get().getImagedata());
            imageName = imageData.get().getName();
        }

        return SendPhoto.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .photo(new InputFile(photo, imageName))
                .caption(HandleScheduleInfoImpl.scheduleInfo(eventDate, schedule, optionalActivity, optionalRoute))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(handleScheduleInfo.createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.SCHEDULE_INFO;
    }
}
