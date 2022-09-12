package com.bot.sup.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final String REGEX_LETTERS = "^(?![\\d+_@.-]+$)[a-zA-Z а-яёА-ЯЁ+_@.-]*$";
    private static final String REGEX_PHONE = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$";

    public static boolean isValidText(String text) {
        Pattern pattern = Pattern.compile(REGEX_LETTERS);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile(REGEX_PHONE);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
