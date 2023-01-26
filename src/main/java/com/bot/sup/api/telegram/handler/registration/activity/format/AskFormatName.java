package com.bot.sup.api.telegram.handler.registration.activity.format;

import com.bot.sup.api.telegram.handler.registration.KeyboardUtil;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ActivityFormatStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.activity.format.impl.ActivityFormatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class AskFormatName implements ActivityFormatMessageProcessor {
    private final UserStateCache userStateCache;
    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatServiceImpl activityFormatService;
    private final MainMessageProperties mainMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object activityFormat) {
        Long chatId = message.getChatId();

        ((ActivityFormat) activityFormat).setName(message.getText());
        activityFormatService.save((ActivityFormat) activityFormat);

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .keyboardMarkup(CallbackEnum.SUP_ACTIVITY_FORMAT.toString(), mainMessageProperties.getDone());

        return messageService.getReplyMessageWithKeyboard(chatId,
                String.format(activityMessageProperties.getRegisteredActivity(), ((ActivityFormat) activityFormat).getName()),
                keyboardMarkup);
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        userStateCache.getByTelegramId(chatId).setState(ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME);

        return messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();
        return activityFormatService.existsByNameEqualsIgnoreCase(userAnswer);
    }

    @Override
    public ActivityFormatStateEnum getCurrentState() {
        return ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME;
    }
}
