package com.bot.sup.cache;

import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.ActivityType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class ActivityTypeDataCache {
    private final Map<Long, ActivityTypeStateEnum> activityTypeState = new WeakHashMap<>();
    private final Map<Long, ActivityType> activityTypeData = new WeakHashMap<>();
    private final Map<Long, Long> activityTypeForUpdate = new WeakHashMap<>();

    public void setActivityTypeCurrentState(Long chatId, ActivityTypeStateEnum registrationState){
        activityTypeState.put(chatId, registrationState);
    }

    public ActivityTypeStateEnum getActivityTypeCurrentState(Long chatId){
        return activityTypeState.getOrDefault(chatId, ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE);
    }

    public void removeActivityTypeCurrentState(Long chatId) {
        activityTypeState.remove(chatId);
    }

    public ActivityType getActivityTypeProfileData(Long chatId) {
        return activityTypeData.getOrDefault(chatId, new ActivityType());
    }

    public void saveActivityTypeProfileData(Long chatId, ActivityType activityType) {
        activityTypeData.put(chatId, activityType);
    }

    public void saveActivityTypeForUpdate(Long chatId, Long activityTypeId) {
        activityTypeForUpdate.put(chatId, activityTypeId);
    }

    public void removeActivityTypeForUpdate(Long chatId) {
        activityTypeForUpdate.remove(chatId);
    }

    public Long getActivityTypeForUpdate(Long chatId) {
        return activityTypeForUpdate.get(chatId);
    }
}
