package com.bot.sup.api.telegram.handler.registration.description;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.AboutUsStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.repository.AboutUsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingAboutUs implements HandleRegistration {
    private final AboutUsRepository aboutUsRepository;
    private final Map<AboutUsStateEnum, AboutUsMessageProcessor> aboutUsMessageProcessorMap;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();
        Optional<AboutUs> aboutUs = aboutUsRepository.findById(1L);
        UserState userState = userStateCache.getByTelegramId(chatId);

        if (userState.getState().equals(AboutUsStateEnum.FILLING_ABOUT_US)) {
            userState.setState(AboutUsStateEnum.START_PROCESSOR);
        }

        return processInputMessage(message, chatId, aboutUs);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message message, Long chatId, Optional<AboutUs> aboutUs) {
        AboutUsStateEnum aboutUsCurrentState = (AboutUsStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = aboutUsMessageProcessorMap.get(aboutUsCurrentState);
        AboutUs ab = aboutUs.orElseGet(AboutUs::new);

        if (messageProcessor.isMessageInvalid(message)) {
            return messageProcessor.processInvalidInputMessage(chatId);
        }

        return messageProcessor.processInputMessage(message, ab);
    }

    @Override
    public Enum<?> getType() {
        return AboutUsStateEnum.FILLING_ABOUT_US;
    }
}
