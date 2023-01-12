package com.bot.sup.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum PaymentStatusEnum {
    PAID("Оплачено"),
    NOT_PAID("Не оплачено"),
    REFUND_REQUESTED("Запрошен возврат"),
    CANCEL_WITHOUT_REFUND("Отмена без возврата"),
    RETURNED("Возвращено");
    private String title;


    PaymentStatusEnum(String s) {
        this.title = s;
    }

    public static List<String> getTitles(){
        List<String> titles = new ArrayList<>();
        for (PaymentStatusEnum value: PaymentStatusEnum.values()) {
            titles.add(value.getTitle());
        }
        return titles;
    }

    public String getTitle() {
        return title;
    }
}
