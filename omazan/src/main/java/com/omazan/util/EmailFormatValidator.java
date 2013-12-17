package com.omazan.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 public class EmailFormatValidator {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"

            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

     public EmailFormatValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }
 
    public static boolean validate(final String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}