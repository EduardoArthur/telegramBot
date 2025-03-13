package com.example.telegrambot.service;

import com.example.telegrambot.exception.InvalidCommandException;
import com.example.telegrambot.strategy.CommandStrategySelector;
import com.example.telegrambot.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${telegram.bot.token}")
    private String BOT_TOKEN;

    @Value("${telegram.bot.username}")
    private String BOT_USERNAME;

    @Autowired
    private CommandStrategySelector commandStrategySelector;

    @Autowired
    private ValidationService validationService;

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            validationService.validateRequest(update);
            commandStrategySelector.handleUpdate(update, this);
        } catch (InvalidCommandException e) {
            sendMessage(update.getMessage().getChatId().toString(), e.getMessage());
        }

    }

    public void sendMessage(String chatId, String text) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Erro ao enviar mensagem:", e);
        }
    }

}
