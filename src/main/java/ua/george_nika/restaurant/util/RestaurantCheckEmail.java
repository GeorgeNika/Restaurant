package ua.george_nika.restaurant.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantCheckEmail {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean validate(final String email) {
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
