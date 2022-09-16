package com.bot.sup.service;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.api.telegram.handler.InstructorStateContext;
import com.bot.sup.cache.impl.InstructorDataCache;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.model.common.TelegramProperties;
import com.bot.sup.mapper.CallbackMap;
import com.bot.sup.mapper.CommandMap;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class Bot extends TelegramLongPollingBot {
    final TelegramProperties config;
    private final CallbackMap callbackMap;
    private final CommandMap commandMap;
    private final InstructorDataCache instructorDataCache;
    private BotApiMethod<?> replyMessage;
    private  final InstructorStateContext instructorStateContext;

    @Override
    public String getBotUsername() {
        return config.getNameBot();
    }

    @Override
    public String getBotToken() {
        return config.getTokenBot();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        RegistrationInstructorStateEnum registrationInstructorStateEnum;

        if (update.hasCallbackQuery()) {
            Callback callback = callbackMap.getCallback(update.getCallbackQuery().getData().split("/")[0]);
            log.info("callback = " + update.getCallbackQuery().getData());
            execute(callback.getCallbackQuery(update.getCallbackQuery()));
        } else if (update.hasMessage()) {
            Long chatId = message.getChatId();
            if (message.getText().startsWith("/")) {
                Handle command = commandMap.getCommand(message.getText());
                execute(command.getMessage(update));
            }else {
                registrationInstructorStateEnum = instructorDataCache.getInstructorCurrentState(chatId);
                log.info("state = " + registrationInstructorStateEnum);
                replyMessage = instructorStateContext.processInputMessage(registrationInstructorStateEnum, message);
                execute(replyMessage);
            }
        }
    }
}
