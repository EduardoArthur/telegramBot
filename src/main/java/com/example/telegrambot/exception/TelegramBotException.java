package com.example.telegrambot.exception;

public class TelegramBotException extends RuntimeException {

    private final int errorCode;

    public TelegramBotException(String message) {
        super(message);
        this.errorCode = 500;
    }

    public TelegramBotException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TelegramBotException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 500;
    }

    public TelegramBotException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}