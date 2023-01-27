package com.bot.sup.common.enums.states;

public enum InformationAboutUsStateEnum implements StateEnum<InformationAboutUsStateEnum> {
    START_PROCESSOR,
    FILLING_ABOUT_US,
    ASK_FULL_DESCRIPTION;

    @Override
    public InformationAboutUsStateEnum[] getValues() {
        return InformationAboutUsStateEnum.values();
    }
}
