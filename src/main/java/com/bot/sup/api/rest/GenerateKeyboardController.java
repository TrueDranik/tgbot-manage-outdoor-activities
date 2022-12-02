package com.bot.sup.api.rest;

import com.bot.sup.service.GenerateKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@CrossOrigin("*")
@RestController
@RequestMapping("/keyboard")
@RequiredArgsConstructor
public class GenerateKeyboardController {
    private final GenerateKeyboard generateKeyboard;

    @PostMapping("/main-menu/{telegramId}")
    public void sendMaimMenu(@PathVariable("telegramId") Long telegramId) throws TelegramApiException {
        generateKeyboard.mainMenu(telegramId);
    }

    @PostMapping("/schedule-menu/{telegramId}")
    public void sendScheduleMenu(@PathVariable("telegramId") Long telegramId) throws TelegramApiException {
        generateKeyboard.scheduleMenu(telegramId);
    }

    @PostMapping("/schedule-info")
    public void sendScheduleInfo(Long telegramId, String activityFormatId, String eventDate, String scheduleId) throws TelegramApiException{
        generateKeyboard.scheduleInfo(telegramId, activityFormatId, eventDate, scheduleId);
    }
}
