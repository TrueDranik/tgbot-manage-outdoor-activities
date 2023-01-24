package com.bot.sup.api.telegram.handler.registration.description;

import com.bot.sup.common.enums.AboutUsStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.repository.AboutUsRepository;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AskFullDescription implements AboutUsMessageProcessor {
    private final MessageService messageService;
    private final AboutUsRepository aboutUsRepository;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        ((AboutUs) entity).setFullDescription(message.getText());

        aboutUsRepository.save((AboutUs) entity);

        return messageService.getReplyMessageWithKeyboard(chatId, "✅ Успешно обновили информацию!", keyboardMenu());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, "Кол-во символов превышает 1024!\nВведите снова.");
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();

        return userAnswer.length() > 1024;
    }

    @Override
    public AboutUsStateEnum getCurrentState() {
        return AboutUsStateEnum.ASK_FULL_DESCRIPTION;
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData(CallbackEnum.MENU.toString())
                        .text("Меню")
                        .build()
        ));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }
}
