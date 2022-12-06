package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import static com.bot.sup.common.enums.CallbackEnum.ACTIVITYFORMAT_TO_DATE;

@Service
@RequiredArgsConstructor
public class CallbackActivityFormatToDateImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final ScheduleRepository scheduleRepository;
    private final ActivityFormatRepository activityFormatRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(ACTIVITYFORMAT_TO_DATE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];

        Optional<ActivityFormat> activityFormat = activityFormatRepository.findById(Long.parseLong(activityFormatId));
        List<Schedule> eventDate = scheduleRepository.getSchedulesByActivity_ActivityFormat_Id(Long.valueOf(activityFormatId));

        if (generateKeyboardWithSchedule(eventDate, activityFormatId).getKeyboard().size() <= 1) {
            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(chatId)
                    .text(String.format(scheduleMessageProperties.getNotFoundFormat(), activityFormat.get().getName()))
                    .parseMode("Markdown")
                    .replyMarkup(generateKeyboardWithSchedule(eventDate, activityFormatId))
                    .build();
        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(String.format(scheduleMessageProperties.getDateChoice(), activityFormat.get().getName()))
                .parseMode("Markdown")
                .replyMarkup(generateKeyboardWithSchedule(eventDate, activityFormatId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithSchedule(List<Schedule> eventDate, String activityFormatId) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        Set<LocalDate> localDates = new HashSet<>();
        for (int indexSchedule = 0; indexSchedule < eventDate.size(); indexSchedule++) {
            if (eventDate.get(indexSchedule).getEventDate().isBefore(LocalDate.now())
                    || eventDate.get(indexSchedule).getActive().equals(false)) {
                continue;
            }

            localDates.add(eventDate.get(indexSchedule).getEventDate());
        }

        localDates.forEach(i -> {
            rowMain.add(InlineKeyboardButton.builder()
                    .text(i.format(formatter) + " (" +
                            i.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru")) + ")")
                    .callbackData(CallbackEnum.DATE_TO_ROUTE + "/" + activityFormatId + "/" + i)
                    .build());
            if (rowMain.size() == 2) {
                List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                mainKeyboard.add(temporaryKeyboardRow);
                rowMain.clear();
            }
        });

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT + "/" + activityFormatId)
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
