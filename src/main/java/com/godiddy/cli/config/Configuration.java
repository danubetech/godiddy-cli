package com.godiddy.cli.config;

import com.godiddy.cli.GodiddyCLIApplication;

import java.util.prefs.Preferences;

public class Configuration {

    private static final Preferences preferences = Preferences.userNodeForPackage(GodiddyCLIApplication.class);

    public static String getApiKey() {
        return preferences.get("apiKey", null);
    }

    public static void setApiKey(String apiKey) {
        if (apiKey == null) {
            preferences.remove("apiKey");
        } else {
            preferences.put("apiKey", apiKey);
        }
    }

    public static String getEndpoint() {
        return preferences.get("endpoint", null);
    }

    public static void setEndpoint(String endpoint) {
        if (endpoint == null) {
            preferences.remove("endpoint");
        } else {
            preferences.put("endpoint", endpoint);
        }
    }
}
