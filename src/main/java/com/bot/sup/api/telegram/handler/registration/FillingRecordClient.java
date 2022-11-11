package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.ClientRecordDataCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.entity.Client;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.MessageService;
import com.bot.sup.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingRecordClient implements HandleRegistration {
    private final MessageService messageService;
    private final ClientRecordDataCache clientRecordDataCache;
    private final ClientRepository clientRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();
        Client client = clientRecordDataCache.getClientProfileData(chatId);

        if (clientRecordDataCache.getClientRecordCurrentState(chatId).equals(ClientRecordStateEnum.FILLING_CLIENT)) {
            clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.ASK_FULL_NAME);
        }

        return processInputMessage(message, chatId);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message message, Long chatId) {
        BotApiMethod<?> replyToUser = null;
        //Long chatId = message.getChatId();
        String userAnswer = message.getText();
        Client client = clientRecordDataCache.getClientProfileData(chatId);
        ClientRecordStateEnum clientRecordCurrentState = clientRecordDataCache.getClientRecordCurrentState(chatId);
        Long scheduleId = clientRecordDataCache.getScheduleState(chatId);

        if (clientRecordCurrentState.equals(ClientRecordStateEnum.ASK_FULL_NAME)) {
            replyToUser = messageService.buildReplyMessage(chatId, "Введите ФИ клиента");
            clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.ASK_PHONE_NUMBER);

            return replyToUser;
        } else if (clientRecordCurrentState.equals(ClientRecordStateEnum.ASK_PHONE_NUMBER)) {
            if (Validation.isValidText(userAnswer)) {
                try {
                    String[] fullName = userAnswer.split(" ");

                    client.setFirstName(fullName[0]);
                    client.setLastName(fullName[1]);

                    if (client.getFirstName().length() < 2 || client.getFirstName().length() > 15
                            && client.getLastName().length() < 2 || client.getLastName().length() > 15) {
                        return messageService.buildReplyMessage(message.getChatId(), "Неккоректный ввод!");
                    }
                } catch (IndexOutOfBoundsException e) {
                    return messageService.buildReplyMessage(chatId, "Пустой ввод!");
                }

                log.info("client Name = " + userAnswer);

                replyToUser = messageService.buildReplyMessage(chatId, "Введите номер телефона формата +79123456789");

                clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.ASK_TELEGRAM_ID);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, "💢 Допустимы только *кириллица* и *английский*!");
                clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.ASK_PHONE_NUMBER);

                return replyToUser;
            }
        } else if (clientRecordCurrentState.equals(ClientRecordStateEnum.ASK_TELEGRAM_ID)) {
            if (Validation.isValidPhoneNumber(userAnswer)) {
                client.setPhoneNumber(userAnswer);

                log.info("client phone number = " + userAnswer);

                replyToUser = messageService.buildReplyMessage(chatId,
                        "Перешлите сообщения для получения TelegramId");

                clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.REGISTERED_CLIENT);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, "Неверный формат номера");
                clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.ASK_TELEGRAM_ID);

                return replyToUser;
            }
        } else if (clientRecordCurrentState.equals(ClientRecordStateEnum.REGISTERED_CLIENT)) {
            Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());

            if (forwardFrom.isPresent() && clientRepository.existsByTelegramId(forwardFrom.get().getId())) {
                replyToUser = messageService.buildReplyMessage(chatId, "Клиент с таким telegramId уже существует!");
                clientRecordDataCache.setClientRecrodCurrentState(chatId, ClientRecordStateEnum.REGISTERED_CLIENT);

                return replyToUser;
            }

            client.setTelegramId(message.getForwardFrom().getId());
            client.setUsername(message.getForwardFrom().getUserName());
            client.setSchedule(Collections.singleton(scheduleRepository.findById(scheduleId)
                    .orElseThrow(() -> new EntityNotFoundException("Schedule not found"))));
            replyToUser = messageService.getReplyMessageWithKeyboard(message.getChatId(), "Клиент записан!", keyboardMarkup());
            clientRepository.save(client);
        }
        clientRecordDataCache.saveClientProfile(chatId, client);

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
