package com.example.telegrambot.controller;

import com.example.telegrambot.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping(value = "/webhook")
public class WebhookController {

    @Autowired
    private TelegramBotService telegramBotService;

    @PostMapping("/postMessage")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBotService.onWebhookUpdateReceived(update);
    }

    @GetMapping("/ping")
    public void ping() {
    }
}
