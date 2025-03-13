package com.example.telegrambot.strategy;

import com.example.telegrambot.service.TelegramBotService;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandStrategy {

    String getCommand();
    String getDescription();
    default boolean supports(String message){
        return message.equalsIgnoreCase(getCommand());
    }

    void execute(Update update, TelegramBotService bot);

}
