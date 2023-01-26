package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.model.entity.Client;
import com.bot.sup.service.booking.BookingServiceImpl;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackClientInfoImpl implements Callback {
    private final ClientServiceImpl clientService;
    private final BookingServiceImpl bookingService;
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];
        String clientId = callbackQuery.getData().split("/")[4];

        Client client = clientService.findById(Long.valueOf(clientId));
        Booking invitedUsers = bookingService.findBookingByClientAndByScheduleId(Long.valueOf(scheduleId), Long.valueOf(clientId));

        String paymentStatus = invitedUsers.getPaymentStatus();
        String userId = String.format("<a href=\"tg://user?id=%s\"> (профиль)</a>", client.getTelegramId().toString());

        int invitedUsers1 = invitedUsers.getInvitedUsers() - 1;

        String s = invitedUsers1 > 0 ? scheduleMessageProperties.getInvitedFriends() + invitedUsers1 + "\n" : "";

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(client.getFirstName() + " " + client.getLastName() + userId + "\n"
                        + "☎️" + client.getPhoneNumber() + "\n"
                        + s
                        + "\uD83D\uDCB8 Статус оплаты: " + paymentStatus)
                .parseMode(ParseMode.HTML)
                .replyMarkup(createInlineKeyboard(client, activityFormatId, eventDate, scheduleId))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(Client client, String activityFormatId, String eventDate, String scheduleId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getConnect())
                .url("https://t.me/" + client.getUsername())
                .build());
        firstRow.add(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getCancelReservation())
                .callbackData(CallbackEnum.SCHEDULE_CLIENT_CANCEL + "/" + activityFormatId + "/" + eventDate + "/"
                        + scheduleId + "/" + client.getId())
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
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.CLIENT_INFO;
    }
}
