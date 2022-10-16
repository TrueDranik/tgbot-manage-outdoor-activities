package com.bot.sup.api.telegram.command;

import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import com.bot.sup.model.common.properties.message.MenuMessageProperties;
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
    private final MenuMessageProperties menuMessageProperties;
    @Override
    public BotCommand getBotCommand() {
        return BotCommand.builder()
                .command("start")
                .description("начать/восстановить работу с ботом")
                .build();
    }

    @Override
    public BotApiMethod<?> getAction(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(menuMessageProperties.getUserChoose())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .build();
    }
}
