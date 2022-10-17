package com.bot.sup.api.telegram.command;

import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
@RequiredArgsConstructor
public class StartCommand implements BaseCommand {
    private final HandleMainMenuImpl handleMainMenu;
    private final MainMessageProperties mainMessageProperties;
    @Override
    public BotCommand getBotCommand() {
        return BotCommand.builder()
                .command(mainMessageProperties.getCommandStart())
                .description(mainMessageProperties.getCommandStartDescription())
                .build();
    }

    @Override
    public BotApiMethod<?> getAction(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(mainMessageProperties.getUserChoose())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .parseMode("Markdown")
                .build();
    }
}
