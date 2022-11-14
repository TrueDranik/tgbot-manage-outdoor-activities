package com.bot.sup.cache;

import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.model.entity.ActivityFormat;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class ActivityFormatDataCache {
    private final Map<Long, ActivityFormatStateEnum> activityFormatState = new WeakHashMap<>();
    private final Map<Long, ActivityFormat> activityFormatData = new WeakHashMap<>();
    private final Map<Long, Long> activityFormatForUpdate = new WeakHashMap<>();

    public void setActivityFormatCurrentState(Long chatId, ActivityFormatStateEnum registrationState) {
        activityFormatState.put(chatId, registrationState);
    }

    public ActivityFormatStateEnum getActivityFormatCurrentState(Long chatId) {
        return activityFormatState.getOrDefault(chatId, ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT);
    }

    public void removeActivityFormatCurrentState(Long chatId) {
        activityFormatState.remove(chatId);
    }

    public ActivityFormat getActivityFormatProfileData(Long chatId) {
        return activityFormatData.getOrDefault(chatId, new ActivityFormat());
    }

    public void saveActivityFormatProfileData(Long chatId, ActivityFormat activityFormat) {
        activityFormatData.put(chatId, activityFormat);
    }

    public void saveActivityFormatForUpdate(Long chatId, Long activityFormatId) {
        activityFormatForUpdate.put(chatId, activityFormatId);
    }

    public void removeActivityFormatForUpdate(Long chatId) {
        activityFormatForUpdate.remove(chatId);
    }

    public Long getActivityFormatForUpdate(Long chatId) {
        return activityFormatForUpdate.get(chatId);
    }
}
