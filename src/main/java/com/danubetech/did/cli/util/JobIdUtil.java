package com.danubetech.did.cli.util;

import com.danubetech.did.cli.config.Api;

import java.util.Map;

public class JobIdUtil {

    public static Object jobIdFromJobIdString(String stringJobId) {
        try {
            return Api.fromJson(stringJobId, Map.class);
        } catch (IllegalArgumentException ex) {
            return stringJobId;
        }
    }
}
