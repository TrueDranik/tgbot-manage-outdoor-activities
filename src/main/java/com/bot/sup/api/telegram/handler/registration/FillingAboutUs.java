package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.AboutUsDataCache;
import com.bot.sup.common.enums.AboutUsStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.repository.AboutUsRepository;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingAboutUs implements HandleRegistration{
    private final AboutUsDataCache aboutUsDataCache;
    private final AboutUsRepository aboutUsRepository;
    private final MessageService messageService;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();

//        AboutUs aboutUs = aboutUsDataCache.getAboutUsProfileData(chatId);

        if (AboutUsStateEnum.FILLING_ABOUT_US.equals(aboutUsDataCache.getAboutUsCurrentState(chatId))){
            aboutUsDataCache.setAboutUsCurrentState(chatId, AboutUsStateEnum.ASK_FULL_DESCRIPTION);
        }

        return processInputMessage(message, chatId /*aboutUs*/);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message message, Long chatId/*, AboutUs aboutUs*/){
        BotApiMethod<?> replyToUser = null;
        String userAnswer = message.getText();
        AboutUsStateEnum aboutUsState = aboutUsDataCache.getAboutUsCurrentState(chatId);

        if (AboutUsStateEnum.ASK_FULL_DESCRIPTION.equals(aboutUsState)){
            replyToUser = messageService.buildReplyMessage(chatId, "Введите информацию \"О нас\"");
            aboutUsDataCache.setAboutUsCurrentState(chatId, AboutUsStateEnum.REGISTERED_INFORMATION);

            return replyToUser;
        } else if (AboutUsStateEnum.REGISTERED_INFORMATION.equals(aboutUsState)){
            Optional<AboutUs> aboutUs = aboutUsRepository.findById(1L);
//            AboutUs ab = aboutUs.get();
            AboutUs ab = aboutUs.isPresent() ? aboutUs.get() : new AboutUs();
            if (userAnswer.length()<=1024) {
                ab.setFullDescription(userAnswer);
                aboutUsRepository.save(ab);
                replyToUser = messageService.getReplyMessageWithKeyboard(chatId, "✅ Успешно обновили информацию!", keyboardMenu());
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, "Кол-во символов превышает 1024!\nВведите снова.");
                aboutUsDataCache.setAboutUsCurrentState(chatId, AboutUsStateEnum.REGISTERED_INFORMATION);
            }
        }

        return replyToUser;
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


    @Override
    public Enum<?> getType() {
        return AboutUsStateEnum.FILLING_ABOUT_US;
    }
}
