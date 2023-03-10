package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
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
import java.util.Optional;

import static com.bot.sup.common.enums.CallbackEnum.ACTIVITY_FORMAT_OPTION;

@RequiredArgsConstructor
@Service
public class CallbackActivityFormatOptionImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityFormatServiceImpl activityFormatService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];

        Optional<ActivityFormat> activityFormat = Optional.ofNullable(activityFormatService.findActivityFormatById(Long.valueOf(activityFormatId)));
        String activityFormatName = activityFormat.isEmpty() ? "Формат активности не найден" : activityFormat.get().getName();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activityFormatName)
                .replyMarkup(generateKeyboardWithActivity(activityFormatId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(String activityFormatId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getChange())
                        .callbackData(CallbackEnum.ACTIVITY_FORMAT_CHANGE + "/" + activityFormatId)
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getDelete())
                        .callbackData(CallbackEnum.DELETE_ACTIVITY_FORMAT + "/" + activityFormatId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_ACTIVITY_FORMAT.toString())
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITY_FORMAT_OPTION;
    }
}
