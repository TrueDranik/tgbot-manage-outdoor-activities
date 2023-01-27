package com.bot.sup;

import com.bot.sup.common.enums.PaymentTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PaymentTypeEnumTest extends AbstractTest{

    @Test
    void getTitles() {
        PaymentTypeEnum.getTitles();
        PaymentTypeEnum.getTitles();
        Map<PaymentTypeEnum, String> titles = PaymentTypeEnum.getTitles();
        assertEquals(4, titles.size());
    }
}