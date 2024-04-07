package com.godiddy.cli.cliconfig;

import com.godiddy.cli.GodiddyCLIApplication;
import com.godiddy.cli.api.Formatting;
import com.godiddy.cli.api.Headers;

import java.util.prefs.Preferences;

public class CLIConfig {

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

    public static Formatting.Value getFormatting() {
        return preferences.get("formatting", null) == null ? null : Formatting.Value.valueOf(preferences.get("formatting", null));
    }

    public static void setFormatting(Formatting.Value formatting) {
        if (formatting == null) {
            preferences.remove("formatting");
        } else {
            preferences.put("formatting", formatting.name());
        }
    }

    public static Headers.Value getHeaders() {
        return preferences.get("headers", null) == null ? null : Headers.Value.valueOf(preferences.get("headers", null));
    }

    public static void setHeaders(Headers.Value headers) {
        if (headers == null) {
            preferences.remove("headers");
        } else {
            preferences.put("headers", headers.name());
        }
    }
}
