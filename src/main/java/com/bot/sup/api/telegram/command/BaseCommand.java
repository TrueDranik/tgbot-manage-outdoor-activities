package com.bot.sup.api.telegram.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public interface BaseCommand {
    BotCommand getBotCommand();
    BotApiMethod<?> getAction(Update update);
}
