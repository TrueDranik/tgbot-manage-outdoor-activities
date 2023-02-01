package com.bot.sup.common.enums.states;

public interface StateEnum<T extends Enum<T>>{
    T[] getValues();
}
