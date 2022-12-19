package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.model.entity.SelectedSchedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.SelectedScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackDateToRouteImpl implements Callback {
    private final ScheduleRepository scheduleRepository;
    private final SelectedScheduleRepository selectedScheduleRepository;
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;

    private static final CallbackEnum ACTIVITIES = CallbackEnum.DATE_TO_ROUTE;

    @Transactional
    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];

        String datePattern = "dd.MM.yyyy";
        String parseMod = "Markdown";

        Optional<SelectedSchedule> selectedActivity = selectedScheduleRepository.findByTelegramId(chatId);
        if (selectedActivity.isPresent()) {
            selectedScheduleRepository.deleteByTelegramId(chatId);
        }


        List<Schedule> schedules = scheduleRepository
                .selectScheduleByActivityFormatIdAndEventDate(Long.valueOf(activityFormatId), LocalDate.parse(eventDate));

        if (createInlineKeyboard(schedules, activityFormatId, eventDate).getKeyboard().size() <= 1) {
            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(chatId)
                    .text(String.format(scheduleMessageProperties.getNotFoundDate(),
                            LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern(datePattern))))
                    .parseMode(parseMod)
                    .replyMarkup(createInlineKeyboard(schedules, activityFormatId, eventDate))
                    .build();
        }

        if (callbackQuery.getMessage().hasPhoto()) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(scheduleMessageProperties.getChooseRouteForDate(),
                            LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern(datePattern)),
                            LocalDate.parse(eventDate).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))))
                    .parseMode(parseMod)
                    .replyMarkup(createInlineKeyboard(schedules, activityFormatId, eventDate))
                    .build();

        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(String.format(scheduleMessageProperties.getChooseRouteForDate(),
                        LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern(datePattern)),
                        LocalDate.parse(eventDate).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))))
                .parseMode(parseMod)
                .replyMarkup(createInlineKeyboard(schedules, activityFormatId, eventDate))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<Schedule> schedules, String activityFormatId, String eventDate) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        schedules.forEach(i -> {
            if (i.getIsActive().equals(true)) {
                rowMain.add(InlineKeyboardButton.builder()
                        .text(i.getRoute().getName() + "|" + i.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .callbackData(CallbackEnum.SCHEDULE_INFO + "/" + activityFormatId + "/" + eventDate + "/" + i.getId())
                        .build());
                if (rowMain.size() == 2) {
                    List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                    mainKeyboard.add(temporaryKeyboardRow);
                    rowMain.clear();
                }
            }
        });

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
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
