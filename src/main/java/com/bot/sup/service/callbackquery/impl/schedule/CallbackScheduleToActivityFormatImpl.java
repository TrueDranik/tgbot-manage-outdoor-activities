package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bot.sup.common.enums.CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT;

@RequiredArgsConstructor
@Service
public class CallbackScheduleToActivityFormatImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final ActivityFormatRepository activityFormatRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(SCHEDULE_TO_ACTIVITYFORMAT);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<ActivityFormat> activityFormats = activityFormatRepository.findAll();
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Список форматов")
                .replyMarkup(generateKeyboardWithActivity(activityFormats))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<ActivityFormat> activityFormats) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        activityFormats.forEach(i -> {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getName())
                            .callbackData(CallbackEnum.ACTIVITYFORMAT_TO_DATE + "/" + i.getId())
                            .build());
                    if (rowMain.size() == 2) {
                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                        mainKeyboard.add(temporaryKeyboardRow);
                        rowMain.clear();
                    }
                }
        );

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SCHEDULE.toString())
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