package com.bot.sup.service;

import com.bot.sup.api.telegram.command.BaseCommand;
import com.bot.sup.api.telegram.handler.registration.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.CallbackMap;
import com.bot.sup.common.properties.TelegramProperties;
import com.bot.sup.model.UserState;
import com.bot.sup.service.callbackquery.Callback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@EnableCaching
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final CallbackMap callbackMap;
    private final StateContext stateContext;
    private final UserStateCache userStateCache;
    private final List<BaseCommand> commands;
    final TelegramProperties config;

    @Getter
    @Setter
    private Update userUpdate;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        setUserUpdate(update);

        if (update.hasCallbackQuery()) {
            Callback callback = callbackMap.getCallback(update.getCallbackQuery().getData().split("/")[0]);

            log.info("callback = {}", update.getCallbackQuery().getData());

            Object callbackQuery = callback.getCallbackQuery(update.getCallbackQuery());

            if (Objects.equals(callbackQuery.getClass(), SendMessage.class)) {
                execute(getDeleteMessage(update));
                execute((SendMessage) callbackQuery);
            } else if (SendPhoto.class.equals(callbackQuery.getClass())) {
                execute(getDeleteMessage(update));
                execute((SendPhoto) callbackQuery);
            } else if (EditMessageText.class.equals(callbackQuery.getClass())) {
                execute((EditMessageText) callbackQuery);
            } else if (DeleteMessage.class.equals(callbackQuery.getClass())) {
                execute((DeleteMessage) callbackQuery);
            } else {
                execute((EditMessageText) callback.getCallbackQuery(update.getCallbackQuery()));
            }
        } else if (update.hasMessage()) {
            Long chatId = message.getChatId();

            log.info("chatId from message = {}", chatId);

            if (message.isCommand()) {
                BaseCommand baseCommand = commands.stream()
                        .filter(it -> message.getText().equals("/" + it.getBotCommand().getCommand()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No such command"));

                execute(baseCommand.getAction(update));
            } else {
                UserState userState = userStateCache.getByTelegramId(chatId);
                Enum<?> state = userState.getState();

                log.info("state = {}", state);

                BotApiMethod<?> replyMessage = stateContext.processInputMessage(state, message);
                execute(replyMessage);
            }
        }
    }

    private static DeleteMessage getDeleteMessage(Update update) {
        return DeleteMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .build();
    }

    @PostConstruct
    @SneakyThrows
    public void initCommands() {
        List<BotCommand> botCommands = commands.stream()
                .map(BaseCommand::getBotCommand).toList();

        SetMyCommands myCommands = SetMyCommands
                .builder()
                .scope(BotCommandScopeDefault.builder().build())
                .commands(botCommands)
                .build();
        execute(myCommands);
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
}
