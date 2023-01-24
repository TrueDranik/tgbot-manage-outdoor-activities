package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.AboutUsStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.repository.AboutUsRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackAddAboutUsInfoImpl implements Callback {
    private final StateContext stateContext;
    private final UserStateCache userStateCache;
    private final AboutUsRepository aboutUsRepository;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        AboutUsStateEnum botStateEnum = AboutUsStateEnum.FILLING_ABOUT_US;
        Optional<AboutUs> aboutUs = aboutUsRepository.findById(1L);

        UserState userState = new UserState();
        userState.setAdminTelegramId(chatId);
        userState.setState(botStateEnum);
        userState.setEntity(aboutUs);
        userState.setForUpdate(false);

        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ADD_ABOUT_US_INFO;
    }
}
