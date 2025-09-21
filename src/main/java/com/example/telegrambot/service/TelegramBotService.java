package com.example.telegrambot.service;

import com.example.telegrambot.exception.InvalidCommandException;
import com.example.telegrambot.strategy.CommandStrategySelector;
import com.example.telegrambot.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TelegramBotService extends SpringWebhookBot {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String botToken;

    private final String botUsername;

    private final String botWebhookPath;

    private final CommandStrategySelector commandStrategySelector;

    private final ValidationService validationService;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botWebhookPath;
    }

    public TelegramBotService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            @Value("${telegram.bot.webhook-path}") String botPath,
            SetWebhook setWebhook,
            CommandStrategySelector commandStrategySelector,
            ValidationService validationService) {
        super(setWebhook, botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.botWebhookPath = botPath;
        this.commandStrategySelector = commandStrategySelector;
        this.validationService = validationService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            validationService.validateRequest(update);
            commandStrategySelector.handleUpdate(update, this);
        } catch (InvalidCommandException e) {
            sendMessage(update.getMessage().getChatId().toString(), e.getMessage());
        }
        return null;
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

    public InputStream loadResource(String path) throws IOException {
        return new ClassPathResource(path).getInputStream();
    }

    public void sendPhoto(String chatId, InputStream imageStream, String fileName, String caption) {
        try {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(imageStream, fileName))
                    .caption(caption)
                    .build();

            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Erro ao enviar foto:", e);
            sendMessage(chatId, "Erro ao enviar a imagem");
        }
    }

}
