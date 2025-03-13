package com.example.telegrambot.exception;

public class InvalidCommandException extends TelegramBotException {
    public InvalidCommandException(String message) {
        super(message);
    }
}
