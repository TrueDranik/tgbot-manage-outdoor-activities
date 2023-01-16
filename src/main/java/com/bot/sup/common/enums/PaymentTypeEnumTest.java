package com.bot.sup.common.enums;

import nonapi.io.github.classgraph.utils.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class PaymentTypeEnumTest {

    @Test
    void getTitles() {
        PaymentTypeEnum.getTitles();
        PaymentTypeEnum.getTitles();
        Map<PaymentTypeEnum, String> titles = PaymentTypeEnum.getTitles();
        assertEquals(4, titles.size());
    }
}