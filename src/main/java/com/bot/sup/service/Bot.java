package com.bot.sup.service;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.cache.SupActivityDataCache;
import com.bot.sup.model.common.*;
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
    private final MiddlewareDataCache middlewareDataCache;
    private final InstructorDataCache instructorDataCache;
    private final SupActivityDataCache supActivityDataCache;
    private final StateContext stateContext;
    private BotApiMethod<?> replyMessage;

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
        InstructorStateEnum instructorStateEnum;
        SupActivityStateEnum supActivityStateEnum;

        if (update.hasCallbackQuery()) {
            Callback callback = callbackMap.getCallback(update.getCallbackQuery().getData().split("/")[0]);

            log.info("callback = " + update.getCallbackQuery().getData());

            execute(callback.getCallbackQuery(update.getCallbackQuery()));
        } else if (update.hasMessage()) {
            Long chatId = message.getChatId();

            log.info("chatId from message = " + chatId);

            instructorDataCache.removeInstructorForUpdate(chatId);
            if (message.getText().startsWith("/")) {
                Handle command = commandMap.getCommand(message.getText());
                execute(command.getMessage(update));
            } else if (middlewareDataCache.getCurrentData(chatId) instanceof InstructorStateEnum) {
                instructorStateEnum = instructorDataCache.getInstructorCurrentState(chatId);

                log.info("state = " + instructorStateEnum);

                replyMessage = stateContext.processInputMessage(instructorStateEnum, message);
                execute(replyMessage);
            } else if (middlewareDataCache.getCurrentData(chatId) instanceof SupActivityStateEnum) {
                supActivityStateEnum = supActivityDataCache.getActivityCurrentState(chatId);

                log.info("state = " + supActivityStateEnum);

                replyMessage = stateContext.processInputMessage(supActivityStateEnum, message);
                execute(replyMessage);
            }
        }
    }
}
