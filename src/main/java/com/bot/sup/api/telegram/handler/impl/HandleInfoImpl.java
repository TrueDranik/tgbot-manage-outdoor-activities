package com.bot.sup.api.telegram.handler.impl;

import com.bot.sup.api.telegram.handler.CommandHandle;
import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.service.callbackquery.ActivityEnum;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HandleInfoImpl implements Handle, CommandHandle {

    @Override
    public BotApiMethod<?> sendCommandMessage(Update update) {
        String textToSend = "Привет дорогой друг";
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText(textToSend);

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> Buttons = new ArrayList<>();

        InlineKeyboardButton schedule = new InlineKeyboardButton("Расписание");
        InlineKeyboardButton instructors = new InlineKeyboardButton("Инструкторы");
        InlineKeyboardButton activities = new InlineKeyboardButton("Активности");

        keyboard.add(Buttons);
        Buttons.add(schedule);
        Buttons.add(instructors);
        Buttons.add(activities);


        schedule.setCallbackData("SCHEDULE");
        instructors.setCallbackData("INSTRUCTORS");
        activities.setCallbackData("ACTIVITIES");

        firstRow.add(schedule);
        secondRow.add(instructors);
        thirdRow.add(activities);

        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .keyboardRow(thirdRow)
                .build();
        message.setReplyMarkup(build);

        return message;
    }

    @Override
    public InlineKeyboardMarkup createButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> Buttons = new ArrayList<InlineKeyboardButton>();

        InlineKeyboardButton youtube = new InlineKeyboardButton("youtube");
        InlineKeyboardButton github = new InlineKeyboardButton("github");

        youtube.setUrl("https://www.youtube.com");
        github.setUrl("https://github.com");

        Buttons.add(youtube);
        Buttons.add(github);
        keyboard.add(Buttons);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

}