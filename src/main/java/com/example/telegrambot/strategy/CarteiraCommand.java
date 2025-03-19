package com.example.telegrambot.strategy;

import com.example.telegrambot.service.CarteiraService;
import com.example.telegrambot.service.TelegramBotService;
import com.example.telegrambot.service.UserStateManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CarteiraCommand implements CommandStrategy {

    private final UserStateManager userStateManager;

    private final CarteiraService carteiraService;

    public CarteiraCommand(UserStateManager userStateManager, CarteiraService carteiraService) {
        this.userStateManager = userStateManager;
        this.carteiraService = carteiraService;
    }

    @Override
    public String getCommand() {
        return "/carteira";
    }

    @Override
    public String getDescription() {
        return "My personal finance app.";
    }

    @Override
    public void execute(Update update, TelegramBotService bot) {

        Long chatId = update.getMessage().getChatId();

        // Define que o usuário está dentro do fluxo do /carteira
        userStateManager.setUserState(chatId, UserStateManager.BotState.AWAIT_OPTION, getCommand());

        bot.sendMessage(chatId.toString(), getMenuMessage());

    }

    @Override
    public void handleResponse(Update update, TelegramBotService bot) {

        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (!getCommand().equals(userStateManager.getActiveCommand(chatId))) {
            return;
        }

        UserStateManager.BotState currentState = userStateManager.getUserState(chatId);

        if (currentState == UserStateManager.BotState.AWAIT_OPTION) {
            processOption(chatId, messageText, bot);
        }

    }

    private String getMenuMessage() {

        StringBuilder builder = new StringBuilder("Selecione uma opção:\n");

        builder.append("1 - Visualizar meus investimentos\n");
        builder.append("2 - Adicionar novo investimento");

        return builder.toString();
    }

    private void processOption(Long chatId, String messageText, TelegramBotService bot) {

        switch (messageText) {

            default:
                bot.sendMessage(chatId.toString(), "Opção inválida!");
                userStateManager.clearUserState(chatId);
                break;

            case "1":
                bot.sendMessage(chatId.toString(), "Consultando seus investimentos...");
                bot.sendMessage(chatId.toString(), "Carteira: " + carteiraService.getCarteira());
                userStateManager.clearUserState(chatId);
                break;

            case "2":
                bot.sendMessage(chatId.toString(), "Digite os detalhes do novo investimento:");
                break;

        }

    }


}

