package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.ClientRecordDataCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.entity.Client;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FillingRecordClient implements HandleRegistration {
    private final MessageService messageService;
    private final ClientRecordDataCache clientRecordDataCache;
    private final ClientRepository clientRepository;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();

        if (clientRecordDataCache.getClientRecordCurrentState(chatId).equals(ClientRecordStateEnum.FILLING_CLIENT)) {
            clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.ASK_CLIENT_RECORD);
        }

        return processInputMessage(message);
    }

    private BotApiMethod<?> processInputMessage(Message message) {
        BotApiMethod<?> replyToUser = null;
        Client client = new Client();
        String userAnswer = message.getText();
        ClientRecordStateEnum clientRecordStateEnum = clientRecordDataCache.getClientRecordCurrentState(message.getChatId());

        if (clientRecordStateEnum.equals(ClientRecordStateEnum.ASK_CLIENT_RECORD)) {
            replyToUser = messageService.buildReplyMessage(message.getChatId(), "Перешлите любое текстовое сообщение клиента");
            clientRecordDataCache.setClientRecrodCurrentState(message.getChatId(), ClientRecordStateEnum.REGISTERED_CLIENT);

            return replyToUser;
        } else if (clientRecordStateEnum.equals(ClientRecordStateEnum.REGISTERED_CLIENT)) {
            Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());

            if (forwardFrom.isPresent() && clientRepository.existsByTelegramId(forwardFrom.get().getId())) {
                replyToUser = messageService.buildReplyMessage(message.getChatId(), "Клиент с таким telegramId уже существует!");
                clientRecordDataCache.setClientRecrodCurrentState(message.getChatId(), ClientRecordStateEnum.REGISTERED_CLIENT);

                return replyToUser;
            }

            try {
                client.setTelegramId(message.getForwardFrom().getId());
                client.setUsername(message.getForwardFrom().getUserName());
            } catch (IndexOutOfBoundsException e) {
                return messageService.buildReplyMessage(message.getChatId(), "Вы не переслали сообщение!");
            }

            clientRepository.save(client);
            replyToUser = messageService.getReplyMessageWithKeyboard(message.getChatId(), "Клиент записан!", keyboardMarkup());
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup keyboardMarkup() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData(CallbackEnum.MENU.toString())
                        .text("Зарегистрировано")
                        .build()));
        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public Enum<?> getType() {
        return ClientRecordStateEnum.FILLING_CLIENT;
    }
}
