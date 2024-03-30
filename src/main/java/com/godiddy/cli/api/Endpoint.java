package com.godiddy.cli.api;

import com.godiddy.cli.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class Endpoint {

    private static final Logger log = LogManager.getLogger(Endpoint.class);

    public static final String DEFAULT_ENDPOINT = "https://api.godiddy.com/1.0.0/";
    public static final Map<String, String> PREDEFINED_ENDPOINTS = Map.of(
            "default", DEFAULT_ENDPOINT,
            "godiddy", "https://api.godiddy.com/1.0.0/",
            "godiddy-dev", "https://api.dev.godiddy.com/1.0.0/",
            "godiddy-0.1.0", "https://api.godiddy.com/0.1.0/",
            "godiddy-dev-0.1.0", "https://api.dev.godiddy.com/0.1.0/",
            "dif-universalresolver", "https://dev.uniresolver.io/1.0/",
            "dif-universalregistrar", "https://uniregistrar.io/1.0/"
    );

    public static String getEndpoint() {
        String endpoint = Objects.requireNonNullElse(Configuration.getEndpoint(), DEFAULT_ENDPOINT);
        if (DEFAULT_ENDPOINT.equals(endpoint)) {
            log.info("Using default endpoint.");
        }
        return endpoint;
    }
}
