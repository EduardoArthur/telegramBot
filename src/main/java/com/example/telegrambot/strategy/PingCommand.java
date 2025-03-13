package com.example.telegrambot.strategy;

import com.example.telegrambot.service.TelegramBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class PingCommand implements CommandStrategy {

    @Override
    public String getCommand() {
        return "/ping";
    }

    @Override
    public String getDescription() {
        return "Return 'Pong'";
    }

    @Override
    public void execute(Update update, TelegramBotService bot) {
        bot.sendMessage(update.getMessage().getChatId().toString(), "Pong");
    }

}

