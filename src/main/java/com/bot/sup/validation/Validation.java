package com.bot.sup.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation { // TODO: 12.12.2022 мб назвать utils

    private static final String REGEX_LETTERS = "^(?![\\d+_@.-]+$)[a-zA-Z а-яёА-ЯЁ+_@.-]*$";
    private static final String REGEX_PHONE = "^(\\+7)?[489][0-9]{2}[0-9]{3}[0-9]{2}[0-9]{2}$";

    public static boolean isValidText(String text) {
        Pattern pattern = Pattern.compile(REGEX_LETTERS);
        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(REGEX_PHONE);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }
}
