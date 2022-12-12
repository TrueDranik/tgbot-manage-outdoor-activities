package com.bot.sup.cache;

import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.entity.Client;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class ClientRecordDataCache {
    private final Map<Long, ClientRecordStateEnum> clientRecordState = new WeakHashMap<>();
    private final Map<Long, Client> clientData = new WeakHashMap<>();
    private final Map<Long, Long> scheduleState = new WeakHashMap<>();

    public void setClientRecordCurrentState(Long chatId, ClientRecordStateEnum registrationState) {
        clientRecordState.put(chatId, registrationState);
    }

    public ClientRecordStateEnum getClientRecordCurrentState(Long chatId) {
        return clientRecordState.getOrDefault(chatId, ClientRecordStateEnum.FILLING_CLIENT);
    }

    public Client getClientProfileData(Long chatId) {
        return clientData.getOrDefault(chatId, new Client());
    }

    public void saveClientProfile(Long chatId, Client client) {
        clientData.put(chatId, client);
    }

    public void setScheduleState(Long chatId, Long scheduleId) {
        scheduleState.put(chatId, scheduleId);
    }

    public Long getScheduleState(Long chatId) {
        return scheduleState.get(chatId);
    }
}
