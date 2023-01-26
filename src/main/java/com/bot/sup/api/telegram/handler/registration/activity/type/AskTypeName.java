package com.bot.sup.api.telegram.handler.registration.activity.type;

import com.bot.sup.api.telegram.handler.registration.KeyboardUtil;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.activity.type.impl.ActivityTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class AskTypeName implements ActivityTypeMessageProcessor {
    private final MessageService messageService;
    private final ActivityTypeServiceImpl activityTypeService;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object activityType) {
        Long chatId = message.getChatId();

        ((ActivityType) activityType).setName(message.getText());

        activityTypeService.save((ActivityType) activityType);

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .keyboardMarkup(CallbackEnum.SUP_ACTIVITY_TYPE.toString(), mainMessageProperties.getDone());
        return messageService.getReplyMessageWithKeyboard(chatId,
                String.format(activityMessageProperties.getRegisteredActivity(), ((ActivityType) activityType).getName()),
                keyboardMarkup);
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();
        return activityTypeService.existsByNameEqualsIgnoreCase(userAnswer);
    }

    @Override
    public ActivityTypeStateEnum getCurrentState() {
        return ActivityTypeStateEnum.ASK_ACTIVITY_TYPE_NAME;
    }
}
