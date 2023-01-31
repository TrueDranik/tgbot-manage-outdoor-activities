package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.activity.format.impl.ActivityFormatServiceImpl;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.bot.sup.common.enums.CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT;

@Service
@RequiredArgsConstructor
public class CallbackScheduleToActivityFormatImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final ActivityFormatServiceImpl activityFormatService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<ActivityFormat> activityFormats = activityFormatService.findAll();
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(scheduleMessageProperties.getMenuSchedules())
                .replyMarkup(generateKeyboardWithActivity(activityFormats))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<ActivityFormat> activityFormats) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        activityFormats.forEach(i -> {
                    if (Boolean.TRUE.equals(i.getIsActive())) {
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
                }
        );

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.MENU.toString())
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return SCHEDULE_TO_ACTIVITYFORMAT;
    }
}
