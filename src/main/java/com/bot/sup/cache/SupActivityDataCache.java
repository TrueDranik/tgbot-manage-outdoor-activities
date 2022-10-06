package com.bot.sup.cache;

import com.bot.sup.model.common.SupActivityStateEnum;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class SupActivityDataCache {
    private final Map<Long, SupActivityStateEnum> supActivityState = new WeakHashMap<>();
    private final Map<Long, Long> supActivityForUpdate = new WeakHashMap<>();


    public void setActivityCurrentState(Long chatId, SupActivityStateEnum registrationState) {
        supActivityState.put(chatId, registrationState);
    }

    public SupActivityStateEnum getActivityCurrentState(Long chatId) {
        return supActivityState.getOrDefault(chatId, SupActivityStateEnum.FILLING_ACTIVITY);
    }

    public Long getSupActivityForUpdate(Long chatId) {
        return supActivityForUpdate.get(chatId);
    }
}
