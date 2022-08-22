package com.bot.sup.api.telegram.handler;

import com.bot.sup.service.callbackquery.ActivityEnum;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Collection;

public interface Handle {
    InlineKeyboardMarkup createButtons();
}
