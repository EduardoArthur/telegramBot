package com.example.telegrambot.validation;

import com.example.telegrambot.exception.TelegramBotException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ValidationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${validate.isPrivateUser}")
    private boolean isPrivateUser;

    @Value("${validate.userId}")
    private Long validUserId;

    public void validateRequest(Update update) {

        log.debug("Validating request");

        validateMessage(update);

        if (isPrivateUser) {
            validateUser(update);
        }

    }

    private void validateUser(Update update){
        if (!update.getMessage().getFrom().getId().equals(validUserId)){
            throw new TelegramBotException("Invalid User");
        }
    }

    private void validateMessage(Update update){
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            throw new TelegramBotException("No valid message", HttpStatus.SC_NOT_FOUND);
        }
    }

}
