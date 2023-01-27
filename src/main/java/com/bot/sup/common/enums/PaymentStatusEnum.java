package com.bot.sup.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
@RequiredArgsConstructor
public enum PaymentStatusEnum {
    PAID("Оплачено"),
    NOT_PAID("Не оплачено"),
    REFUND_REQUESTED("Запрошен возврат"),
    CANCEL_WITHOUT_REFUND("Отмена без возврата"),
    PREPAID("Внесена предоплата"),
    RETURNED("Возвращено");

    private final String title;


    private static final Map<PaymentStatusEnum, String> titles = new HashMap<>(PaymentStatusEnum.values().length);

    static {
        for (PaymentStatusEnum value : PaymentStatusEnum.values()) {
            titles.put(value, value.getTitle());
        }

    }

    public static Map<PaymentStatusEnum, String> getTitles() {
        return titles;
    }
}
