package com.example.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TelegramBotService telegramBot;

    public ScheduledTaskService(TelegramBotService telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Value("${telegram.chat.id}")
    private String CHAT_ID;

    /**
     * Cron para o dia 28 de abril às 12:00 (meio-dia)
     * Formato: segundo minuto hora dia mês diaDaSemana
     */
    @Scheduled(cron = "0 0 12 28 4 ?")
    public void sendPingCommand() {
        log.info("Executing Scheduled Task");
        telegramBot.sendMessage(CHAT_ID, "Mensagem Automatica");
    }
}

