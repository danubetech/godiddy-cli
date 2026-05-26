package com.danubetech.did.cli.util;

import com.danubetech.did.cli.config.Api;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OptionsUtil {

    public static Map<String, Object> optionsFromOptionStrings(Map<String, String> stringOptions) {
        return stringOptions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> optionFromOptionValueString(e.getValue())));
    }

    public static Object optionFromOptionValueString(String stringOptionValue) {
        if (String.valueOf(true).equals(stringOptionValue)) return Boolean.TRUE;
        if (String.valueOf(false).equals(stringOptionValue)) return Boolean.FALSE;
        if (NumberUtils.isCreatable(stringOptionValue)) return NumberUtils.createNumber(stringOptionValue);
        try {
            return Api.fromJson(stringOptionValue, Map.class);
        } catch (IllegalArgumentException ignored) { }
        try {
            return Api.fromJson(stringOptionValue, List.class);
        } catch (IllegalArgumentException ignored) { }
        return stringOptionValue;
    }
}
