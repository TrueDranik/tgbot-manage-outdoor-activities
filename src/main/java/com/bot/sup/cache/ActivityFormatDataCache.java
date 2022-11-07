package com.bot.sup.cache;

import com.bot.sup.common.enums.ActivityFormatStateEnum;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class ActivityFormatDataCache {
    private final Map<Long, ActivityFormatStateEnum> activityFormatState = new WeakHashMap<>();
    private final Map<Long, Long> activityFormatForUpdate = new WeakHashMap<>();

    public void setActivityFormatCurrentState(Long chatId, ActivityFormatStateEnum registrationState) {
        activityFormatState.put(chatId, registrationState);
    }

    public ActivityFormatStateEnum getActivityFormatCurrentState(Long chatId) {
        return activityFormatState.getOrDefault(chatId, ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT);
    }

    public Long getActivityFormatForUpdate(Long chatId) {
        return activityFormatForUpdate.get(chatId);
    }
}
