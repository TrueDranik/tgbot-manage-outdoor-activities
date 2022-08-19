package com.bot.sup.service;

import com.bot.sup.config.properties.TelegramProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    final TelegramProperties config;

    @Override
    public String getBotUsername() { return config.getNameBot(); }

    @Override
    public String getBotToken() {
        return config.getTokenBot();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId);
                    break;
            }
        }
    }

    private void startCommandReceived(long chatId){
        String answer = "Расписание(кнопка)\n" + "Инструкторы(кнопка)\n" + "Активности(кнопка)\n";

        sendMessage(chatId, answer);
    }

    private SendMessage sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
        }

        return message;
    }
}
