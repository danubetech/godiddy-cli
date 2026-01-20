package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Endpoint {

    private static final Logger log = LogManager.getLogger(Endpoint.class);

    public static final String DEFAULT_ENDPOINT = "https://api.godiddy.com/1.0.0/";
    public static final Map<String, String> PREDEFINED_ENDPOINTS;

    static {
        PREDEFINED_ENDPOINTS = new HashMap<>();
        PREDEFINED_ENDPOINTS.put("def", DEFAULT_ENDPOINT);
        PREDEFINED_ENDPOINTS.put("godiddy", "https://api.godiddy.com/1.0.0/");
        PREDEFINED_ENDPOINTS.put("godiddy-dev", "https://api.dev.godiddy.com/1.0.0/");
        PREDEFINED_ENDPOINTS.put("dif-universalresolver", "https://dev.uniresolver.io/1.0/");
        PREDEFINED_ENDPOINTS.put("dif-universalregistrar", "https://uniregistrar.io/1.0/");
        PREDEFINED_ENDPOINTS.put("local-universalresolver", "http://localhost:8080/1.0/");
        PREDEFINED_ENDPOINTS.put("local-universalregistrar", "http://localhost:9080/1.0/");
        PREDEFINED_ENDPOINTS.put("local-businesswallet", "http://localhost:21080/1.0.0/");
        PREDEFINED_ENDPOINTS.put("docker-universalresolver", "http://172.17.0.1:8080/1.0/");
        PREDEFINED_ENDPOINTS.put("docker-universalregistrar", "http://172.17.0.1:9080/1.0/");
        PREDEFINED_ENDPOINTS.put("docker-businesswallet", "http://172.17.0.1:21080/1.0.0/");
    }

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

    public static Boolean guessEndpointRaw() {
        String endpoint = getEndpoint();
        if (endpoint == null) return null;
        if (endpoint.contains("universal-resolver")) return Boolean.TRUE;
        if (endpoint.contains("universal-registrar")) return Boolean.TRUE;
        if (endpoint.contains("uni-resolver")) return Boolean.TRUE;
        if (endpoint.contains("uni-registrar")) return Boolean.TRUE;
        if (endpoint.contains("uniresolver.io")) return Boolean.TRUE;
        if (endpoint.contains("uniregistrar.io")) return Boolean.TRUE;
        if (endpoint.contains(":8080")) return Boolean.TRUE;
        if (endpoint.contains(":9080")) return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
