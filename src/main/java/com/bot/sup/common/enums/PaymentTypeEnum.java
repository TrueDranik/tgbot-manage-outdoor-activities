package com.bot.sup.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum PaymentTypeEnum {

    BANK("Банк"),
    CASH("Наличные"),
    PAYMENT_BY_DETAILS("Реквизиты"),
    TRANSFER("Перевод");
    private String title;

    PaymentTypeEnum(String s) {
    }

    public String getTitle() {
        return title;
    }

    public static List<String> getTitles(){
        List<String> titles = new ArrayList<>();
        for (PaymentTypeEnum value: PaymentTypeEnum.values()) {
            titles.add(value.getTitle());
        }
        return titles;
    }

    @Override
    public String toString() {
        return title;
    }
}
