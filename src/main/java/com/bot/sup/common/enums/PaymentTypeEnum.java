package com.bot.sup.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@RequiredArgsConstructor
public enum PaymentTypeEnum {

    BANK("Банк"),
    CASH("Наличные"),
    PAYMENT_BY_DETAILS("Реквизиты"),
    TRANSFER("Перевод");

    private final String title;

    private static final List<String> titles = new ArrayList<>(PaymentTypeEnum.values().length);

    public static List<String> getTitles() {
        if (!titles.isEmpty()) {
            return titles;
        }

        for (PaymentTypeEnum value : PaymentTypeEnum.values()) {
            titles.add(value.getTitle());
        }

        return titles;
    }


}
