package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.model.entity.Client;
import com.bot.sup.service.booking.BookingServiceImpl;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.client.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackScheduleAllClientInfoImpl implements Callback {
    private final ClientServiceImpl clientService;
    private final BookingServiceImpl bookingService;
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String activityFormatId = callbackQuery.getData().split("/")[1];
        String eventDate = callbackQuery.getData().split("/")[2];
        String scheduleId = callbackQuery.getData().split("/")[3];

        List<Client> clientByScheduleId = clientService.findClientByScheduleId(Long.valueOf(scheduleId));
        Long sumBookingClients = bookingService.findSumBookingClientByScheduleId(Long.valueOf(scheduleId));

        if (callbackQuery.getMessage().hasPhoto()) {
            return SendMessage.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(String.format(scheduleMessageProperties.getClientsRecorded(), sumBookingClients))
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(createInlineKeyboard(clientByScheduleId, activityFormatId, eventDate, scheduleId))
                    .build();
        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text(String.format(scheduleMessageProperties.getClientsRecorded(), sumBookingClients))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(createInlineKeyboard(clientByScheduleId, activityFormatId, eventDate, scheduleId))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<Client> clientByScheduleId, String activityFormatId,
                                                      String eventDate, String scheduleId) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        clientByScheduleId.forEach(c -> {
                    firstRow.add(InlineKeyboardButton.builder()
                            .text(c.getFirstName() + " " + c.getLastName())
                            .callbackData(CallbackEnum.CLIENT_INFO + "/" + activityFormatId + "/" + eventDate + "/"
                                    + scheduleId + "/" + c.getId())
                            .build());
                    if (firstRow.size() > 1) {
                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(firstRow);
                        mainKeyboard.add(temporaryKeyboardRow);
                        firstRow.clear();
                    }
                }
        );

        if (firstRow.size() == 1) {
            mainKeyboard.add(firstRow);
        }

        secondRow.add(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getRecordClient())
                .callbackData(CallbackEnum.CLIENT_RECORD + "/" + scheduleId)
                .build());
        secondRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SCHEDULE_INFO + "/" + activityFormatId + "/" + eventDate + "/" + scheduleId)
                .build());
        mainKeyboard.add(secondRow);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.SCHEDULE_ALL_CLIENT_INFO;
    }
}
