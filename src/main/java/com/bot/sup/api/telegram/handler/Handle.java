package com.bot.sup.api.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Handle {
    BotApiMethod<?> getMessage(Update update);
    InlineKeyboardMarkup createInlineKeyboard();
}
