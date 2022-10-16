package com.bot.sup.service;

import com.bot.sup.api.telegram.command.BaseCommand;
import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.cache.SupActivityDataCache;
import com.bot.sup.model.common.CallbackMap;
import com.bot.sup.model.common.InstructorStateEnum;
import com.bot.sup.model.common.SupActivityStateEnum;
import com.bot.sup.model.common.properties.TelegramProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class Bot extends TelegramLongPollingBot {
    private final CallbackMap callbackMap;
    private final MiddlewareDataCache middlewareDataCache;
    private final InstructorDataCache instructorDataCache;
    private final SupActivityDataCache supActivityDataCache;
    private final StateContext stateContext;
    private final List<BaseCommand> commands;
    private BotApiMethod<?> replyMessage;

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
            if (message.isCommand()) {
                BaseCommand baseCommand = commands.stream()
                        .filter(it -> message.getText().equals("/" + it.getBotCommand().getCommand()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No such command"));
                execute(baseCommand.getAction(update));
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

    @PostConstruct
    @SneakyThrows
    public void initCommands() {
        List<BotCommand> botCommands = commands.stream()
                .map(BaseCommand::getBotCommand)
                .collect(Collectors.toList());

        SetMyCommands myCommands = SetMyCommands
                .builder()
                .scope(BotCommandScopeDefault.builder().build())
                .commands(botCommands)
                .build();
        execute(myCommands);
    }

    @Override
    public String getBotUsername() {
        return config.getNameBot();
    }

    @Override
    public String getBotToken() {
        return config.getTokenBot();
    }
}
