package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallbackDateToRouteImpl implements Callback {
    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final ActivityRepository activityRepository;
    private final MainMessageProperties mainMessageProperties;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.DATE_TO_ROUTE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];

//        List<Schedule> schedules = scheduleRepository.findAll();
        //Optional<Schedule> schedule = scheduleRepository.findById(Long.parseLong(scheduleId));
        List<Schedule> schedules = scheduleRepository.selectScheduleByActivityFormatId(Long.valueOf(activityFormatId));
        List<Schedule> eventTime = scheduleRepository.findByEventDate(LocalDate.parse(eventDate));
//        Optional<Route> route = activityRepository.selectOneRouteByActivityFormatId(Long.valueOf(activityFormatId));


        //LocalTime localTime = eventTime.getEventTime();
        if (createInlineKeyboard(schedules, eventTime, activityFormatId, eventDate).getKeyboard().size() <= 1) {
            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(chatId)
                    .text("❌ Расписание для *" + "route.get().getName()" + "* отсутствует.\nВернитесь назад.")
                    .parseMode("Markdown")
                    .replyMarkup(createInlineKeyboard(schedules, eventTime, activityFormatId, eventDate))
                    .build();
        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Выберите маршрут для даты *" + LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "*")
                .parseMode("Markdown")
                .replyMarkup(createInlineKeyboard(schedules, eventTime, activityFormatId, eventDate))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<Schedule> schedules, List<Schedule> eventTime, String activityFormatId, String eventDate) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        for (int n = 0; n < eventTime.size(); n++) {
            int finalN = n;
            schedules.forEach(i -> {
                if (i.getEventTime().equals(eventTime.get(finalN).getEventTime())) {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getActivity().getRoute().getName() + "|" + i.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                            .callbackData(CallbackEnum.SCHEDULE_INFO + "/" + activityFormatId + "/" + eventDate + "/" + i.getId())
                            .build());
                    if (rowMain.size() == 2) {
                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                        mainKeyboard.add(temporaryKeyboardRow);
                        rowMain.clear();
                    }
                }
            });
        }

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.ACTIVITYFORMAT_TO_DATE + "/" + activityFormatId + "/" + eventDate)
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
