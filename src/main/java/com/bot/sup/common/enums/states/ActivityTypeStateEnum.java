package com.bot.sup.common.enums.states;

public enum ActivityTypeStateEnum implements StateEnum<ActivityTypeStateEnum> {
    START_PROCESSING,
    ASK_ACTIVITY_TYPE_NAME,
    FILLING_ACTIVITY_TYPE;

    @Override
    public ActivityTypeStateEnum[] getValues() {
        return ActivityTypeStateEnum.values();
    }
}
