package com.bot.sup.model.common;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandMap {
    private static final Map<String, Handle> COMMAND_MAP = new HashMap<>();

    public CommandMap() {
        COMMAND_MAP.put("/start", new HandleMainMenuImpl());
    }

    public Handle getCommand(String keyCommand) {
        return COMMAND_MAP.get(keyCommand);
    }
}
