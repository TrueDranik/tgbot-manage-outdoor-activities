package com.bot.sup.api.telegram.handler.registration.activity.format;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
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
public class AskFormatName implements ActivityFormatMessageProcessor {
    private final UserStateCache userStateCache;
    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatRepository activityFormatRepository;
    private final MainMessageProperties mainMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object activityFormat) {
        Long chatId = message.getChatId();

        ((ActivityFormat) activityFormat).setName(message.getText());

        activityFormatRepository.save((ActivityFormat) activityFormat);

        return messageService.getReplyMessageWithKeyboard(chatId,
                String.format(activityMessageProperties.getRegisteredActivity(), ((ActivityFormat) activityFormat).getName()),
                keyboardMenu());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        userStateCache.getByTelegramId(chatId).setState(ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME);

        return messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();
        return activityFormatRepository.existsByNameEqualsIgnoreCase(userAnswer);
    }

    @Override
    public ActivityFormatStateEnum getCurrentState() {
        return ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME;
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
