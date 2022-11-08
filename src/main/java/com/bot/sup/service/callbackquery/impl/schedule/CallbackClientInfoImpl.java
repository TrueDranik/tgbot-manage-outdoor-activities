package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.model.entity.Client;
import com.bot.sup.repository.BookingRepository;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CallbackClientInfoImpl implements Callback {
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;
    private final MainMessageProperties mainMessageProperties;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.CLIENT_INFO);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];
        String clientId = callbackQuery.getData().split("/")[4];

        Optional<Client> client = clientRepository.findById(Long.valueOf(clientId));
        Optional<Booking> invitedUsers = bookingRepository.findBookingByClient_Id(Long.valueOf(clientId));

        String userId = String.format("<a href=\"tg://user?id=%s\"> (профиль)</a>", client.get().getTelegramId().toString());

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(client.get().getFirstName() + client.get().getLastName() + userId + "\n"
                        + client.get().getPhoneNumber() + "\n"
                        + "Взял с собой друзей: " + invitedUsers.get().getInvitedUsers())
                .parseMode("HTML")
                .replyMarkup(createInlineKeyboard(client, activityFormatId, eventDate, scheduleId))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(Optional<Client> client, String activityFormatId, String eventDate, String scheduleId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text("Связаться")
                .url("https://t.me/" + client.get().getUsername())
                .build());
        firstRow.add(InlineKeyboardButton.builder()
                .text("Отменить бронь")
                .callbackData(CallbackEnum.SCHEDULE_CLIENT_CANCEL + "/" + activityFormatId + "/" + eventDate + "/"
                        + scheduleId + "/" + client.get().getId())
                .build());

        secondRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SCHEDULE_ALL_CLIENT_INFO + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
