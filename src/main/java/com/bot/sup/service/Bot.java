package com.bot.sup.service;

import com.bot.sup.api.telegram.handler.CommandHandle;
import com.bot.sup.config.properties.TelegramProperties;
import com.bot.sup.enums.CallbackMap;
import com.bot.sup.enums.CommandMap;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    final TelegramProperties config;
    private final CallbackMap callbackMap;
    private final CommandMap commandMap;

    @Override
    public String getBotUsername() { return config.getNameBot(); }

    @Override
    public String getBotToken() {
        return config.getTokenBot();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            Callback callback = callbackMap.getCallback(update.getCallbackQuery().getData());
            execute(callback.getCallbackQuery(update.getCallbackQuery()));
        }

        if (update.getMessage().hasText()) {
            CommandHandle command = commandMap.getCommand(update.getMessage().getText());
            execute(command.sendCommandMessage(update));
        }
    }
}
