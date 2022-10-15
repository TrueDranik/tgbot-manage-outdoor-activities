package com.bot.sup.service;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.cache.SupActivityDataCache;
import com.bot.sup.model.common.CallbackMap;
import com.bot.sup.model.common.CommandMap;
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
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonCommands;

import java.util.ArrayList;
import java.util.List;

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

        DeleteMyCommands deleteMyCommands = DeleteMyCommands
                .builder().languageCode("ru").scope(BotCommandScopeDefault.builder().build()).build();
        //execute(deleteMyCommands);

        SetMyCommands setMyCommands = SetMyCommands
                .builder()
                .scope(BotCommandScopeDefault.builder().build())
                .build();
        List<BotCommand> commands = new ArrayList<>();
        commands.add(BotCommand.builder().command("start").description("начать работу с ботом").build());
        commands.add(BotCommand.builder().command("help").description("дополнительная информация").build());
        setMyCommands.setCommands(commands);
        execute(setMyCommands);

        SetChatMenuButton setChatMenuButton = SetChatMenuButton
                .builder()
                .chatId(message.getChatId())
                .menuButton(MenuButtonCommands.builder().build())
                .build();
        execute(setChatMenuButton);

        if (update.hasCallbackQuery()) {
            Callback callback = callbackMap.getCallback(update.getCallbackQuery().getData().split("/")[0]);

            log.info("callback = " + update.getCallbackQuery().getData());

            execute(callback.getCallbackQuery(update.getCallbackQuery()));
        } else if (update.hasMessage()) {
            Long chatId = message.getChatId();
            execute(setMyCommands);
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
