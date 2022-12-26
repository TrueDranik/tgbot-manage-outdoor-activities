package com.bot.sup.api.telegram.command;

import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.properties.message.MainMessageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
@RequiredArgsConstructor
public class StartCommand implements BaseCommand {
    private final HandleMainMenuImpl handleMainMenu;
    private final MainMessageProperties mainMessageProperties;
    private final MiddlewareDataCache middlewareDataCache;

    @Override
    public BotCommand getBotCommand() {
        return BotCommand.builder()
                .command(mainMessageProperties.getCommandStart())
                .description(mainMessageProperties.getCommandStartDescription())
                .build();
    }

    @Override
    public BotApiMethod<?> getAction(Update update) {
        middlewareDataCache.removeCurrentState(update.getMessage().getChatId());

        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(mainMessageProperties.getUserChoose())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .parseMode(ParseMode.MARKDOWN)
                .build();
    }
}
