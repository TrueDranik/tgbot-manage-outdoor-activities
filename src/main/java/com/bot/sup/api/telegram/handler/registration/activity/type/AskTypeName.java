package com.bot.sup.api.telegram.handler.registration.activity.type;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AskTypeName implements ActivityTypeMessageProcessor {
    private final UserStateCache userStateCache;
    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityTypeRepository activityTypeRepository;
    private final MainMessageProperties mainMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object activityType) {
        Long chatId = message.getChatId();

        ((ActivityType) activityType).setName(message.getText());

        activityTypeRepository.save((ActivityType) activityType);

        return messageService.getReplyMessageWithKeyboard(chatId,
                String.format(activityMessageProperties.getRegisteredActivity(), ((ActivityType) activityType).getName()),
                keyboardMenu());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();
        return activityTypeRepository.existsByNameEqualsIgnoreCase(userAnswer);
    }

    @Override
    public ActivityTypeStateEnum getCurrentState() {
        return ActivityTypeStateEnum.ASK_ACTIVITY_TYPE_NAME;
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData(CallbackEnum.SUP_ACTIVITY_FORMAT.toString())
                        .text(mainMessageProperties.getDone())
                        .build()
        ));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }
}
