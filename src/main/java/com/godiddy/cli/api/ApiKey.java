package com.godiddy.cli.api;

import com.godiddy.cli.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;

public class ApiKey {

    private static final Logger log = LogManager.getLogger(ApiKey.class);

    public static final String DEFAULT_APIKEY = "b082c420-df67-4b06-899c-b7c51d75fba0";
    public static final Map<String, String> PREDEFINED_APIKEYS = Map.of(
            "def", DEFAULT_APIKEY
    );

    public static String getApiKey() {
        String apiKey = Objects.requireNonNullElse(Configuration.getApiKey(), DEFAULT_APIKEY);
        if (DEFAULT_APIKEY.equals(apiKey)) {
            log.warn("Warning: Using default API key, which can only be used in a limited way, so you may encounter restrictions. Consider setting your own API key.");
        }
        return apiKey;
    }
}
