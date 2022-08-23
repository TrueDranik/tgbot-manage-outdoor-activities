package com.bot.sup.enums;

import com.bot.sup.api.telegram.handler.CommandHandle;
import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.api.telegram.handler.impl.HandleInfoImpl;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandMap {
    private static final Map<String, CommandHandle> COMMAND_MAP = new HashMap<>();

    public CommandMap() {
        COMMAND_MAP.put("/start", new HandleInfoImpl());
    }

    public CommandHandle getCommand(String keyCommand) {
        return COMMAND_MAP.get(keyCommand);
    }
}

