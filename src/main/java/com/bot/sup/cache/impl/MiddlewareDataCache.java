package com.bot.sup.cache.impl;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class MiddlewareDataCache {
    Map<Long, Object> activityCache = new WeakHashMap<>();

    public void validCurrentState(Long chatId, Object currentState){
        activityCache.put(chatId, currentState);
    }

    public Object getCurrentData(Long chatId){
        return activityCache.get(chatId);
    }
}
