package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Client;
import com.bot.sup.service.MessageService;
import com.bot.sup.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskClientFullName implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final UserStateCache userStateCache;
    private String errorMessage = "";

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();
        String[] fullName = userAnswer.split(" ");

        UserState cacheByTelegramId = userStateCache.getByTelegramId(chatId);

        ((Client) client).setFirstName(fullName[0]);
        ((Client) client).setLastName(fullName[1]);

        log.info("client Name = " + userAnswer);

        cacheByTelegramId.setState(ClientRecordStateEnum.ASK_PHONE_NUMBER);

        return messageService.buildReplyMessage(chatId, "Введите номер телефона формата +79123456789");
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, errorMessage + "\nВведите ФИ заново!");
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String messageText = message.getText();
        String[] split = messageText.split(" ");
        boolean invalidFirstName = split[0].length() < 2 || split[0].length() > 15;
        boolean invalidLastName = split[1].length() > 15 || split[1].length() > 15;

        boolean validText = !Validation.isValidText(messageText);
        boolean validFio = !StringUtils.hasText(messageText) || invalidFirstName || invalidLastName;
        if (validText) {
            errorMessage = "💢 Допустимы только *кириллица* и *английский*!";
        } else if (validFio) {
            errorMessage = "Длина слова от 2 до 15 символов!";
        }

        return validText || validFio;
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_FULL_NAME;
    }
}
