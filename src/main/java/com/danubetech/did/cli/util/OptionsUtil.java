package com.danubetech.did.cli.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;
import java.util.stream.Collectors;

public class OptionsUtil {

    public static Map<String, Object> optionsFromOptionStrings(Map<String, String> stringOptions) {
        return stringOptions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> optionFromOptionString(e.getValue())));
    }

    public static Object optionFromOptionString(String stringOption) {
        if (String.valueOf(true).equals(stringOption)) return Boolean.TRUE;
        if (String.valueOf(false).equals(stringOption)) return Boolean.FALSE;
        if (NumberUtils.isCreatable(stringOption)) return NumberUtils.createNumber(stringOption);
        return stringOption;
    }
}
