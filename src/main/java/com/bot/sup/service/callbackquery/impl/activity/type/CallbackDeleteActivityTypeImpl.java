package com.bot.sup.service.callbackquery.impl.activity.type;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static com.bot.sup.common.enums.CallbackEnum.DELETE_ACTIVITY_TYPE;

@Service
@RequiredArgsConstructor
public class CallbackDeleteActivityTypeImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityTypeRepository activityTypeRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(DELETE_ACTIVITY_TYPE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityId = callbackQuery.getData().split("/")[1];
        deleteActivity(Long.parseLong(activityId));

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .text(activityMessageProperties.getDeleteActivity())
                .chatId(chatId)
                .replyMarkup(createKeyboardForDeleteActivity())
                .build();
    }

    private InlineKeyboardMarkup createKeyboardForDeleteActivity() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData("SUP_ACTIVITY_TYPE")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    private void deleteActivity(Long chatId) {
        activityTypeRepository.deleteById(chatId);
    }


    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}