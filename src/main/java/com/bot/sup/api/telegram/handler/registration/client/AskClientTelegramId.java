package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.api.telegram.handler.registration.KeyboardUtil;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.ClientRecordStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Client;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AskClientTelegramId implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final ClientRepository clientRepository;
    private final ClientServiceImpl clientService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);

//        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());

//        Optional<Client> clientByTelegramId = clientService.findByTelegramId(forwardFrom.get().getId());

//        Client clientChoice = clientByTelegramId.get();

        ((Client) client).setTelegramId(message.getForwardFrom().getId());
        ((Client) client).setUsername(message.getForwardFrom().getUserName());

        userState.setState(ClientRecordStateEnum.ASK_FULL_NAME);

        return messageService.buildReplyMessage(chatId, "Введите ФИ клиента");
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
//        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
//        Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());
        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .keyboardMarkup(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString(), "Зарегистрировано");

        return messageService.getReplyMessageWithKeyboard(chatId, "Найден клиент: " /*+ clientByTelegramId.get().getFirstName()
                + " " + clientByTelegramId.get().getLastName()*/, keyboardMarkup);
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
        Long telegramId = forwardFrom.get().getId();
        Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(telegramId);

        return clientRepository.existsByTelegramId(telegramId) && clientByTelegramId.isPresent();
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_TELEGRAM_ID;
    }
}
