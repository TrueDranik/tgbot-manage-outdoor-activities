package com.bot.sup.common.enums.states;

public enum ClientRecordStateEnum implements StateEnum<ClientRecordStateEnum> {
    START_PROCESSING,
    FILLING_CLIENT,
    ASK_TELEGRAM_ID,
    ASK_FULL_NAME,
    ASK_PHONE_NUMBER,
    ASK_BIRTHDAY;

    @Override
    public ClientRecordStateEnum[] getValues() {
        return ClientRecordStateEnum.values();
    }
}
