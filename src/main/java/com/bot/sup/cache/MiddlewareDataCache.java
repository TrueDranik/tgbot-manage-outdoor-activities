package com.bot.sup.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class MiddlewareDataCache {
    Map<Long, Object> middlewareCache = new WeakHashMap<>();

    public void setValidCurrentState(Long chatId, Object currentState) {
        middlewareCache.put(chatId, currentState);
    }

    public Object getCurrentData(Long chatId) {
        return middlewareCache.get(chatId);
    }

    public void removeCurrentState(Long chatId){
        middlewareCache.remove(chatId);
    }
}
