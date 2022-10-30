package com.bot.sup.cache;

import com.bot.sup.common.enums.ActivityTypeStateEnum;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class ActivityTypeDataCache {
    private final Map<Long, ActivityTypeStateEnum> activityTypeState = new WeakHashMap<>();
    private final Map<Long, Long> activityTypeForUpdate = new WeakHashMap<>();

    public void setActivityTypeCurrentState(Long chatId, ActivityTypeStateEnum registrationState){
        activityTypeState.put(chatId, registrationState);
    }

    public ActivityTypeStateEnum getActivityTypeCurrentState(Long chatId){
        return activityTypeState.getOrDefault(chatId, ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE);
    }

    public Long getActivityTypeForUpdate(Long chatId){
        return activityTypeForUpdate.get(chatId);
    }
}
