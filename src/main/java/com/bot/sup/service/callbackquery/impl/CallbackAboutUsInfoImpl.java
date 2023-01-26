package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.repository.AboutUsRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackAboutUsInfoImpl implements Callback {
    private final AboutUsRepository aboutUsRepository;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        Optional<AboutUs> aboutUs = aboutUsRepository.getAboutUs();
        String fullDescription = aboutUs.isEmpty() || aboutUs.get().getFullDescription().isEmpty()
                ? "❓ Информация отсутствует!" : aboutUs.get().getFullDescription();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(fullDescription)
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⚙️ Редактировать")
                        .callbackData(CallbackEnum.ADD_ABOUT_US_INFO.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("⬅️ Назад")
                        .callbackData(CallbackEnum.MENU.toString())
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }


    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ABOUT_US_INFO;
    }
}
