package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.common.enums.CallbackEnum.LIST_ACTIVITY_FORMAT;

@Service
@RequiredArgsConstructor
public class CallbackListActivityFormatImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatRepository activityFormatRepository;

    public static final CallbackEnum ACTIVITIES = LIST_ACTIVITY_FORMAT;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<List<InlineKeyboardButton>> buttonActivityFormat = new ArrayList<>();

        List<ActivityFormat> activities = activityFormatRepository.findAll();

        if (activities.isEmpty()) {
            buttonActivityFormat.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(mainMessageProperties.getBack())
                            .callbackData(CallbackEnum.SUP_ACTIVITY_FORMAT.toString())
                            .build()
            ));
            InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                    .keyboard(buttonActivityFormat)
                    .build();

            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(activityMessageProperties.getEmptyActivity())
                    .replyMarkup(keyboard)
                    .build();
        }

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(generateKeyboardWithActivity(activities))
                .text(activityMessageProperties.getListActivityFormat())
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<ActivityFormat> activities) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        activities.forEach(i -> {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getName())
                            .callbackData(CallbackEnum.ACTIVITY_FORMAT_OPTION + "/" + i.getId())
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
                .callbackData(CallbackEnum.SUP_ACTIVITY_FORMAT.toString())
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
