package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.SupActivityDataCache;
import com.bot.sup.model.common.InstructorStateEnum;
import com.bot.sup.model.common.SupActivityStateEnum;
import com.bot.sup.model.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.common.properties.message.MenuMessageProperties;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.service.ActivityService;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingActivity implements HandleRegistration {
    private final MessageService messageService;
    private final ActivityService activityService;
    private final SupActivityDataCache supActivityDataCache;
    private final MenuMessageProperties menuMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityRepository activityRepository;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();
        Long supActivityForUpdate = supActivityDataCache.getSupActivityForUpdate(chatId);


        if (supActivityDataCache.getActivityCurrentState(chatId).equals(SupActivityStateEnum.FILLING_ACTIVITY))
            supActivityDataCache.setActivityCurrentState(chatId, SupActivityStateEnum.ASK_ACTIVITY_NAME);

        return processInputMessage(message, chatId);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message inputMessage, Long chatId) {
        BotApiMethod<?> replyToUser = null;
        Activity activity = new Activity();
        String userAnswer = inputMessage.getText();
        SupActivityStateEnum activityCurrentState = supActivityDataCache.getActivityCurrentState(chatId);

        if (activityCurrentState.equals(SupActivityStateEnum.ASK_ACTIVITY_NAME)) {
            replyToUser = messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityName());
            supActivityDataCache.setActivityCurrentState(chatId, SupActivityStateEnum.REGISTERED_ACTIVITY);

            return replyToUser;
        } else if (activityCurrentState.equals(SupActivityStateEnum.REGISTERED_ACTIVITY)) {
            //Optional<String> forwardFrom = inputMessage;

            if (activityRepository.existsByName(userAnswer)){
                replyToUser = messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
                supActivityDataCache.setActivityCurrentState(chatId, SupActivityStateEnum.REGISTERED_ACTIVITY);

                return replyToUser;
            }
            try {


                activity.setName(userAnswer);
            } catch (IndexOutOfBoundsException e) {
                return messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityNameIsEmpty());
            }

            activityService.save(activity);

            replyToUser = messageService.getReplyMessageWithKeyboard(chatId, String
                    .format(activityMessageProperties.getRegisteredActivity(), activity.getName()),
                    keyboardMenu());
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData("SUP_ACTIVITY")
                        .text(menuMessageProperties.getDone())
                        .build()
        ));

        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    @Override
    public SupActivityStateEnum getType() {
        return SupActivityStateEnum.FILLING_ACTIVITY;
    }
}
