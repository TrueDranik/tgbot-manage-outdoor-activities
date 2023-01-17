package com.bot.sup.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class MessageService {
    public SendMessage buildReplyMessage(Long chatId, String text) {
        return SendMessage.builder()
                .text(text)
                .parseMode(ParseMode.MARKDOWN)
                .chatId(chatId.toString())
                .build();
    }

    public SendMessage getReplyMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(keyboard)
                .build();
    }
}
