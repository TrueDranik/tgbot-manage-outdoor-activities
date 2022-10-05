package com.bot.sup.service;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.impl.ActivityDataCache;
import com.bot.sup.cache.impl.InstructorDataCache;
import com.bot.sup.cache.impl.MiddlewareDataCache;
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
import org.yaml.snakeyaml.util.EnumUtils;

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
    private final ActivityDataCache activityDataCache;
    private BotApiMethod<?> replyMessage;
    private final StateContext stateContext;

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
        //BotStateEnum botStateEnum;

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
            } else if (middlewareDataCache.getCurrentData(chatId).equals(instructorDataCache)) {
                instructorStateEnum = instructorDataCache.getInstructorCurrentState(chatId); //TODO: создать кеш, чтобы видеть текущую активность

                log.info("state = " + instructorStateEnum);
                replyMessage = stateContext.processInputMessage(instructorStateEnum, message);
                execute(replyMessage);
            } else if (middlewareDataCache.getCurrentData(chatId).equals(activityDataCache)) {
                supActivityStateEnum = activityDataCache.getActivityCurrentState(chatId);

                log.info("state = " + supActivityStateEnum);
                replyMessage = stateContext.processInputMessage(supActivityStateEnum, message);
                execute(replyMessage);
            }
        }
    }
}
