package com.example.telegrambot.service;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserStateManager {

    private final Map<Long, BotState> userStates = new HashMap<>();
    private final Map<Long, String> activeCommands = new HashMap<>();

    public void setUserState(Long userId, BotState state, String command) {
        userStates.put(userId, state);
        activeCommands.put(userId, command);
    }

    public BotState getUserState(Long userId) {
        return userStates.getOrDefault(userId, null);
    }

    public String getActiveCommand(Long userId) {
        return activeCommands.get(userId);
    }

    public void clearUserState(Long userId) {
        userStates.remove(userId);
        activeCommands.remove(userId);
    }

    public boolean hasActiveCommand(Long userId) {
        return activeCommands.containsKey(userId);
    }

    public enum BotState {
        AWAIT_OPTION,
        AWAIT_DATA
    }
}

