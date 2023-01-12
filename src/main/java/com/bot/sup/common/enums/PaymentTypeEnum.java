package com.bot.sup.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@RequiredArgsConstructor
public enum PaymentTypeEnum {

    BANK("Банк"),
    CASH("Наличные"),
    PAYMENT_BY_DETAILS("Реквизиты"),
    TRANSFER("Перевод");

    private final String title;

    private static final Map<PaymentTypeEnum, String> titles = new HashMap<PaymentTypeEnum, String>(PaymentTypeEnum.values().length);

     static  {
        for (PaymentTypeEnum value : PaymentTypeEnum.values()) {
            titles.put(value, value.getTitle());
        }

    }
    public static Map<PaymentTypeEnum, String> getTitles() {
         return titles;
    }


}
