package com.example.telegrambot.strategy;

import com.example.telegrambot.service.TelegramBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand implements CommandStrategy {

    @Override
    public String getCommand() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "Returns all available commands";
    }

    @Override
    public void execute(Update update, TelegramBotService bot) {

        StringBuilder builder = new StringBuilder("All commands:\n");

        for (CommandStrategy command : CommandStrategySelector.commands.values()) {
            builder.append(command.getCommand())
                    .append(" -> ")
                    .append(command.getDescription())
                    .append("\n");
        }

        bot.sendMessage(update.getMessage().getChatId().toString(), builder.toString());
    }
}

