package com.bot.sup.model.common;

import com.bot.sup.service.callbackquery.Callback;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CallbackMap {
    private static final Map<String, Callback> CALLBACK_MAP = new HashMap<>();

    public CallbackMap(List<Callback> callbacks) {
        callbacks.forEach(c -> c.getSupportedActivities()
                .forEach(c1 -> CALLBACK_MAP.put(c1.name(), c)));
    }

    public Callback getCallback(String keyCallback) {
        return CALLBACK_MAP.get(keyCallback);
    }
}
