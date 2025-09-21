package com.example.telegrambot.strategy;

import com.example.telegrambot.service.TelegramBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;

@Component
public class WakeUpCommand implements CommandStrategy {

    private static final String IMAGE_NAME = "wakeUp.png";

    @Override
    public String getCommand() {
        return "/wakeup";
    }

    @Override
    public String getDescription() {
        return "Wakes up the bot";
    }

    @Override
    public void execute(Update update, TelegramBotService bot) {
        String chatId = update.getMessage().getChatId().toString();

        try {
            InputStream imageStream = bot.loadResource("images/" + IMAGE_NAME);
            bot.sendPhoto(chatId, imageStream, IMAGE_NAME, "Who dares disturb me?");
        } catch ( IOException e) {
            bot.sendMessage(chatId, "Erro ao carregar a imagem 😢");
        }
    }
}
