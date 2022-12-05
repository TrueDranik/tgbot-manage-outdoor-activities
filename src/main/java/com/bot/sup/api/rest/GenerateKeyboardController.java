package com.bot.sup.api.rest;

import com.bot.sup.service.GenerateKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@CrossOrigin("*")
@RestController
@RequestMapping("/keyboard")
@RequiredArgsConstructor
public class GenerateKeyboardController {
    private final GenerateKeyboard generateKeyboard;

    @GetMapping("/message/{telegramId}")
    public ResponseEntity<Integer> getMessageId(@PathVariable("telegramId") Long telegramId) {
        return new ResponseEntity<>(generateKeyboard.getMessageId(telegramId), HttpStatus.OK);
    }

    @PostMapping("/main-menu")
    public void sendMaimMenu(Long telegramId, Integer messageId) throws TelegramApiException {
        generateKeyboard.mainMenu(telegramId, messageId);
    }

    @PostMapping("/schedule-menu")
    public void sendScheduleMenu(Long telegramId, Integer messageId) throws TelegramApiException {
        generateKeyboard.scheduleMenu(telegramId, messageId);
    }

    @PostMapping("/schedule-info")
    public void sendScheduleInfo(Long telegramId, Integer messageId, String activityFormatId, String eventDate, String scheduleId) throws TelegramApiException {
        generateKeyboard.scheduleInfo(telegramId, messageId, activityFormatId, eventDate, scheduleId);
    }
}
