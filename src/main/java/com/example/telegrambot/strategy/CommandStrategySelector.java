package com.example.telegrambot.strategy;

import com.example.telegrambot.exception.InvalidCommandException;
import com.example.telegrambot.service.TelegramBotService;
import com.example.telegrambot.service.UserStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandStrategySelector {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<CommandStrategy> strategies;
    public static Map<String, CommandStrategy> commands = new HashMap<>();

    private final UserStateManager userStateManager;

    @Autowired
    public CommandStrategySelector(List<CommandStrategy> strategies, UserStateManager userStateManager) {
        this.strategies = strategies;
        this.userStateManager = userStateManager;
        registerCommands();
    }


    public void handleUpdate(Update update, TelegramBotService bot) {

        String messageText = update.getMessage().getText().toLowerCase();
        Long chatId = update.getMessage().getChatId();

        if (userStateManager.hasActiveCommand(chatId)) {
            String activeCommand = userStateManager.getActiveCommand(chatId);
            commands.get(activeCommand).handleResponse(update, bot);
            return;
        }

        if (!messageText.startsWith("/")) {
            return;
        }

        if (!commands.containsKey(messageText)) {
            throw new InvalidCommandException("Invalid Command");
        }

        log.debug("Executing command {}", messageText);
        commands.get(messageText).execute(update, bot);

    }

    private void registerCommands() {
        for (CommandStrategy strategy : strategies) {
            addCommand(strategy.getCommand(), strategy);
        }
    }

    private void addCommand(String command, CommandStrategy strategy) {
        commands.put(command, strategy);
    }

}
