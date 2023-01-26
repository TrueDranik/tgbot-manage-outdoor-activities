package com.bot.sup.common.enums.states;

public enum AboutUsStateEnum implements StateEnum<AboutUsStateEnum> {
    START_PROCESSOR,
    FILLING_ABOUT_US,
    ASK_FULL_DESCRIPTION;

    @Override
    public AboutUsStateEnum[] getValues() {
        return AboutUsStateEnum.values();
    }
}
