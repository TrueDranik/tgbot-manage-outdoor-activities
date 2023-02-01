package com.bot.sup.api.telegram.handler.registration.information;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.util.MessageProcessorUtil;
import com.bot.sup.api.telegram.handler.registration.information.states.InformationAboutUsMessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.InformationAboutUsStateEnum;
import com.bot.sup.common.enums.states.StateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.service.InformationAboutUsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingInformationAboutUs implements InformationAboutUsHandleRegistration {
    private final InformationAboutUsService informationAboutUsService;
    private final Map<InformationAboutUsStateEnum, InformationAboutUsMessageProcessor> aboutUsMessageProcessorMap;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        Optional<AboutUs> aboutUs = informationAboutUsService.findById(1L);
        UserState userState = userStateCache.getByTelegramId(chatId);

        if (userState.getState().equals(InformationAboutUsStateEnum.FILLING_ABOUT_US)) {
            userState.setState(InformationAboutUsStateEnum.START_PROCESSOR);
        }

        AboutUs ab = aboutUs.orElseGet(AboutUs::new);
        InformationAboutUsStateEnum aboutUsCurrentState = (InformationAboutUsStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = aboutUsMessageProcessorMap.get(aboutUsCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, ab);
    }

    @Override
    public StateEnum<?> getType() {
        return InformationAboutUsStateEnum.FILLING_ABOUT_US;
    }
}
