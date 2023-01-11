package com.bot.sup.common.enums;

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

    public PaymentTypeEnum[] getAllPaymentType(){
        return PaymentTypeEnum.values();
    }

    @Override
    public String toString() {
        return title;
    }
}
