package com.bot.sup.cache.impl;

import com.bot.sup.model.common.SupActivityStateEnum;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActivityDataCache {
    private final Map<Long, SupActivityStateEnum> activityState = new WeakHashMap<>();
//    private final Map<Long, Activity> activityData = new ConcurrentHashMap<>();

    public void setActivityCurrentState(Long chatId, SupActivityStateEnum registrationState) {
        activityState.put(chatId, registrationState);
    }

    public SupActivityStateEnum getActivityCurrentState(Long chatId) {
        return activityState.getOrDefault(chatId, SupActivityStateEnum.FILLING_ACTIVITY);
    }

//    public Activity getActivityNameData(Long chatId) {
//        return activityData.getOrDefault(chatId, new Activity());
//    }
//
//    public void saveActivityNameData(Long chatId, Activity activity) {
//        activityData.put(chatId, activity);
//    }
}
