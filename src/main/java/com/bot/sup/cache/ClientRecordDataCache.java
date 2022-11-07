package com.bot.sup.cache;

import com.bot.sup.common.enums.ClientRecordStateEnum;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class ClientRecordDataCache {
    private final Map<Long, ClientRecordStateEnum> clientRecordState = new WeakHashMap<>();

    public void setClientRecrodCurrentState(Long chatId, ClientRecordStateEnum registrationState){
        clientRecordState.put(chatId, registrationState);
    }

    public ClientRecordStateEnum getClientRecordCurrentState(Long chatId){
        return clientRecordState.getOrDefault(chatId, ClientRecordStateEnum.FILLING_CLIENT);
    }
}
