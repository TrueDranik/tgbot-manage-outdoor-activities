package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.entity.Client;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AskClientBirthday implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final UserStateCache userStateCache;
    private final ClientRepository clientRepository;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthday = LocalDate.from(formatter.parse(userAnswer));

        ((Client) client).setBirthDate(birthday);

        clientRepository.save((Client) client);

        return messageService.getReplyMessageWithKeyboard(chatId, "Клиент записан!", keyboardMarkup());
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

    private InlineKeyboardMarkup keyboardMarkup() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        buttons.add(
                InlineKeyboardButton.builder()
                        .text("Зарегистрировано")
                        .callbackData(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString())
                        .build());
        return InlineKeyboardMarkup.builder()
                .keyboardRow(buttons)
                .build();
    }
}
