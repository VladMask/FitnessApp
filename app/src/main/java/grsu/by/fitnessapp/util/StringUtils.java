package grsu.by.fitnessapp.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String formatName(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}

