package com.bot.sup.common.enums.states;

public enum ActivityFormatStateEnum implements StateEnum<ActivityFormatStateEnum> {
    START_PROCESSING,
    ASK_ACTIVITY_FORMAT_NAME,
    FILLING_ACTIVITY_FORMAT;

    @Override
    public ActivityFormatStateEnum[] getValues() {
        return ActivityFormatStateEnum.values();
    }
}
