package com.bot.sup.api.telegram.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class HelpCommand implements BaseCommand {
    @Override
    public BotCommand getBotCommand() {
        return BotCommand.builder()
                .command("help")
                .description("дополнительная информация")
                .build();
    }

    @Override
    public BotApiMethod<?> getAction(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Бот предназначен для администрирования активностей!" +
                        "\n/start")
                .parseMode("HTML")
                .build();
    }
}
