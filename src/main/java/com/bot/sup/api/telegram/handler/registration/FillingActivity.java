package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.impl.ActivityDataCache;
//import com.bot.sup.model.common.BotStateEnum;
import com.bot.sup.model.common.SupActivityStateEnum;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.service.ActivityService;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingActivity implements HandleRegistration{
    private final MessageService messageService;
    private final ActivityService activityService;
    private final ActivityDataCache activityDataCache;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();

        if(activityDataCache.getActivityCurrentState(chatId).equals(SupActivityStateEnum.FILLING_ACTIVITY))
            activityDataCache.setActivityCurrentState(chatId, SupActivityStateEnum.ASK_ACTIVITY_NAME);
        
        return processInputMessage(message, chatId);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message inputMessage, Long chatId){
        Activity activity = new Activity();
        String userAnswer = inputMessage.getText();
        SupActivityStateEnum activityCurrentState = activityDataCache.getActivityCurrentState(chatId);
        BotApiMethod<?> replyToUser = null;

        if (activityCurrentState.equals(SupActivityStateEnum.ASK_ACTIVITY_NAME)) {
            replyToUser = messageService.buildReplyMessage(chatId, "Введи наименование активности!");
            activityDataCache.setActivityCurrentState(chatId, SupActivityStateEnum.REGISTERED_ACTIVITY);
            return replyToUser;
        } else if (activityCurrentState.equals(SupActivityStateEnum.REGISTERED_ACTIVITY)) {
            try {
                activity.setName(userAnswer);
            } catch (IndexOutOfBoundsException e) {
                return messageService.buildReplyMessage(chatId, "Вы не ввели название активности!");
            }
            activityService.save(activity);
            replyToUser = messageService.getReplyMessageWithKeyboard(chatId, "Активность зарегистрирована!\n" + activity.getName(), keyboardMenu());
        }



        return replyToUser;
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder().callbackData("SAP_ACTIVITY").text("✅Готово!").build()
        ));

        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    @Override
    public SupActivityStateEnum getType() {
        return SupActivityStateEnum.FILLING_ACTIVITY;
    }
}
