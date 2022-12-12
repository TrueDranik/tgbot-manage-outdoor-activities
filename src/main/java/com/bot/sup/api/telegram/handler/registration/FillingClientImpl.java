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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingClientImpl implements HandleRegistration {
    private final MessageService messageService;
    private final ClientRecordDataCache clientRecordDataCache;
    private final ClientRepository clientRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();

        if (clientRecordDataCache.getClientRecordCurrentState(chatId).equals(ClientRecordStateEnum.FILLING_CLIENT)) {
            clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.ASK_TELEGRAM_ID);
        }

        return processInputMessage(message, chatId);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message message, Long chatId) {
        BotApiMethod<?> replyToUser = null;
        String userAnswer = message.getText();
        Client client = clientRecordDataCache.getClientProfileData(chatId);
        ClientRecordStateEnum clientRecordCurrentState = clientRecordDataCache.getClientRecordCurrentState(chatId);
        Long scheduleId = clientRecordDataCache.getScheduleState(chatId);

        if (clientRecordCurrentState.equals(ClientRecordStateEnum.ASK_TELEGRAM_ID)) {
            replyToUser = messageService.buildReplyMessage(chatId, "Перешлите любое сообщение клиента");
            clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.ASK_FULL_NAME);

            return replyToUser;
        } else if (clientRecordCurrentState.equals(ClientRecordStateEnum.ASK_FULL_NAME)) {
            Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
            Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());

            if (clientRepository.existsByTelegramId(forwardFrom.get().getId()) && clientByTelegramId.isPresent()) {
                clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());
                Client clientChoice = clientByTelegramId.get();

                clientChoice.getSchedule().add(scheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new EntityNotFoundException("Schedule not found")));

                clientRepository.save(clientChoice);
                replyToUser = messageService.getReplyMessageWithKeyboard(chatId, "Найден клиент: " + clientByTelegramId.get().getFirstName()
                        + " " + clientByTelegramId.get().getLastName(), keyboardMarkup());
            } else {
                client.setTelegramId(message.getForwardFrom().getId());
                client.setUsername(message.getForwardFrom().getUserName());

                replyToUser = messageService.buildReplyMessage(chatId, "Введите ФИ клиента.");
                clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.ASK_PHONE_NUMBER);
            }
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

                clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.ASK_BIRTHDAY);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, "💢 Допустимы только *кириллица* и *английский*!");
                clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.ASK_PHONE_NUMBER);

                return replyToUser;
            }
        } else if (clientRecordCurrentState.equals(ClientRecordStateEnum.ASK_BIRTHDAY)) {
            if (Validation.isValidPhoneNumber(userAnswer)) {
                client.setPhoneNumber(userAnswer);

                log.info("client phone number = " + userAnswer);

                replyToUser = messageService.buildReplyMessage(chatId,
                        "Введите дату рождения в формате дд.мм.гггг");

                clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.REGISTERED_CLIENT);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, "Неверный формат номера");
                clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.ASK_TELEGRAM_ID);

                return replyToUser;
            }
        } else if (clientRecordCurrentState.equals(ClientRecordStateEnum.REGISTERED_CLIENT)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate birthday = LocalDate.from(formatter.parse(userAnswer));

                client.setBirthDate(birthday);
                client.setSchedule(Collections.singleton(scheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new EntityNotFoundException("Schedule not found"))));

                clientRepository.save(client);
                replyToUser = messageService.getReplyMessageWithKeyboard(chatId, "Клиент записан!", keyboardMarkup());
            } catch (EntityNotFoundException e) {
                clientRecordDataCache.setClientRecordCurrentState(chatId, ClientRecordStateEnum.REGISTERED_CLIENT);
            }
        }

        clientRecordDataCache.saveClientProfile(chatId, client);

        return replyToUser;
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

    @Override
    public Enum<?> getType() {
        return ClientRecordStateEnum.FILLING_CLIENT;
    }
}
