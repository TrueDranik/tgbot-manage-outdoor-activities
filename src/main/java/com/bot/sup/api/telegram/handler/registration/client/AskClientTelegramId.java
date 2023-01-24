package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.UserState;
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
public class AskClientTelegramId implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final ClientRepository clientRepository;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);

        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
        Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());

        clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());
        Client clientChoice = clientByTelegramId.get();

        ((Client) client).setTelegramId(message.getForwardFrom().getId());
        ((Client) client).setUsername(message.getForwardFrom().getUserName());

        userState.setState(ClientRecordStateEnum.ASK_FULL_NAME);

        return messageService.buildReplyMessage(chatId, "Введите ФИ клиента");
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
//        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
//        Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());

        return messageService.getReplyMessageWithKeyboard(chatId, "Найден клиент: " /*+ clientByTelegramId.get().getFirstName()
                + " " + clientByTelegramId.get().getLastName()*/, keyboardMarkup());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
        Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());

        return clientRepository.existsByTelegramId(forwardFrom.get().getId()) && clientByTelegramId.isPresent();
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_TELEGRAM_ID;
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
