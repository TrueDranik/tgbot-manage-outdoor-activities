package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.Client;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CallbackClientCancelImpl implements Callback {
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final ClientRepository clientRepository;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.SCHEDULE_CLIENT_CANCEL);

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];
        String clientId = callbackQuery.getData().split("/")[4];

        Optional<Client> client = Optional.ofNullable(clientRepository.findById(Long.valueOf(clientId))
                .orElseThrow(() -> new EntityNotFoundException("Clint with id[" + clientId + "] not found")));
        String userId = String.format("<a href=\"tg://user?id=%d\">%s %s</a>", client.get().getTelegramId(),
                client.get().getFirstName(), client.get().getLastName());

        return SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text(String.format(scheduleMessageProperties.getConfirmCancelReservationClient(), userId))
                .parseMode("HTML")
                .replyMarkup(createInlineKeyboard(activityFormatId, eventDate, scheduleId, clientId))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(String activityFormatId, String eventDate, String scheduleId, String clientId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text("Да")
                .callbackData(CallbackEnum.CLIENT_CANCEL_YES + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId + "/" + clientId)
                .build());
        firstRow.add(InlineKeyboardButton.builder()
                .text("Нет")
                .callbackData(CallbackEnum.CLIENT_CANCEL_NO.toString())
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
