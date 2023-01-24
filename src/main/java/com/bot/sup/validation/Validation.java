package com.bot.sup.validation;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Validation {
    private static final String REGEX_LETTERS = "^(?![\\d+_@.-]+$)[a-zA-Z а-яёА-ЯЁ+_@.-]*$";
    private static final String REGEX_PHONE = "^(\\+7)?[489][0-9]{2}[0-9]{3}[0-9]{2}[0-9]{2}$";
    private static final Pattern PATTERN_NAME = Pattern.compile(REGEX_LETTERS);
    private static final Pattern PATTERN_PHONE = Pattern.compile(REGEX_PHONE);

    public static boolean isValidText(String text) {
        Matcher matcher = PATTERN_NAME.matcher(text);

        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Matcher matcher = PATTERN_PHONE.matcher(phoneNumber);

        return matcher.matches();
    }
}
