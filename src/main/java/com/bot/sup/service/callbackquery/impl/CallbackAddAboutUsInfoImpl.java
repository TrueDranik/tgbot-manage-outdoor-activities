package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.registration.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.InformationAboutUsStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.service.InformationAboutUsService;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.bot.sup.util.UserStateUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackAddAboutUsInfoImpl implements Callback {
    private final StateContext stateContext;
    private final UserStateCache userStateCache;
    private final InformationAboutUsService informationAboutUsService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        InformationAboutUsStateEnum botStateEnum = InformationAboutUsStateEnum.FILLING_ABOUT_US;
        Optional<AboutUs> aboutUs = informationAboutUsService.findById(1L);

        UserState userState = UserStateUtil.getUserState(chatId, botStateEnum, aboutUs, false);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ADD_ABOUT_US_INFO;
    }
}
