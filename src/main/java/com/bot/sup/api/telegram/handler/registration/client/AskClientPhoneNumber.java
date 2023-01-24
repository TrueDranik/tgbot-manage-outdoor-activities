package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.entity.Client;
import com.bot.sup.service.MessageService;
import com.bot.sup.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskClientPhoneNumber implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        ((Client) client).setPhoneNumber(userAnswer);

        log.info("instructor phone number = " + userAnswer);

        userStateCache.getByTelegramId(chatId).setState(ClientRecordStateEnum.ASK_BIRTHDAY);

        return messageService.buildReplyMessage(chatId, "Введите дату рождения в формате дд.мм.гггг");
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, "Неверный формат номера");
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return !Validation.isValidPhoneNumber(message.getText());
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_PHONE_NUMBER;
    }
}
