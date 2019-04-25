package com.github.kolyall.javautils;

/**
 * Created by Nick Unuchek on 16.08.2017.
 */

public class EmailUtils {
    public static boolean isValidEmailAddress(String email) {
        if (StringUtils.isEmpty(email)) return false;

        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
