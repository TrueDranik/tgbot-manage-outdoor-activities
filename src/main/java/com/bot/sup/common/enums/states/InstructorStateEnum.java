package com.bot.sup.common.enums.states;

public enum InstructorStateEnum implements StateEnum<InstructorStateEnum> {
    START_PROCESSING,
    ASK_FULL_NAME,
    ASK_PHONE_NUMBER,
    ASK_TELEGRAM_ID,
    FILLING_INSTRUCTOR;

    @Override
    public InstructorStateEnum[] getValues() {
        return InstructorStateEnum.values();
    }
}
