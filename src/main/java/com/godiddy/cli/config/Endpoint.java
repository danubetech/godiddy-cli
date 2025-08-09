package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class Endpoint {

    private static final Logger log = LogManager.getLogger(Endpoint.class);

    public static final String DEFAULT_ENDPOINT = "https://api.godiddy.com/1.0.0/";
    public static final Map<String, String> PREDEFINED_ENDPOINTS = Map.of(
            "def", DEFAULT_ENDPOINT,
            "godiddy", "https://api.godiddy.com/1.0.0/",
            "godiddy-dev", "https://api.dev.godiddy.com/1.0.0/",
            "dif-universalresolver", "https://dev.uniresolver.io/1.0/",
            "dif-universalregistrar", "https://uniregistrar.io/1.0/",
            "local-universalresolver", "http://localhost:8080/1.0/",
            "local-universalregistrar", "http://localhost:9080/1.0/",
            "local-businesswallet", "http://localhost:21080/1.0.0/"
    );

    public static final Boolean DEFAULT_ENDPOINTRAW = Boolean.FALSE;

    public static String getEndpoint() {
        String endpoint = Objects.requireNonNullElse(CLIConfig.getEndpoint(), DEFAULT_ENDPOINT);
        if (DEFAULT_ENDPOINT.equals(endpoint)) {
            log.debug("Using default endpoint: " + DEFAULT_ENDPOINT);
        }
        return endpoint;
    }

    public static Boolean getEndpointRaw() {
        Boolean endpointRaw = Objects.requireNonNullElse(CLIConfig.getEndpointRaw(), DEFAULT_ENDPOINTRAW);
        return endpointRaw;
    }
}
