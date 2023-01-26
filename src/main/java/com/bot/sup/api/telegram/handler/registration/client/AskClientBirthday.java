package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.api.telegram.handler.registration.KeyboardUtil;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.ClientRecordStateEnum;
import com.bot.sup.model.entity.Client;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class AskClientBirthday implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final ClientServiceImpl clientService;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthday = LocalDate.from(formatter.parse(userAnswer));

        ((Client) client).setBirthDate(birthday);

        clientService.save((Client) client);

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .keyboardMarkup(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString(), "Зарегистрировано");

        return messageService.getReplyMessageWithKeyboard(chatId, "Клиент записан!", keyboardMarkup);
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return null;
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return false;
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_BIRTHDAY;
    }
}
