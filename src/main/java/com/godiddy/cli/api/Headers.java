package com.godiddy.cli.api;

import com.godiddy.cli.clidata.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class Headers {

    private static final Logger log = LogManager.getLogger(Headers.class);

    public enum Value {
        off,
        on,
        def
    };

    public static final String DEFAULT_HEADERS = "off";
    public static final Map<String, Value> PREDEFINED_HEADERS = Map.of(
            "def", Value.valueOf(DEFAULT_HEADERS)
    );

    public static Value getHeaders() {
        Value headers = Objects.requireNonNullElse(CLIConfig.getHeaders(), Value.valueOf(DEFAULT_HEADERS));
        if (Value.valueOf(DEFAULT_HEADERS).equals(headers)) {
            log.debug("Using default headers: " + DEFAULT_HEADERS);
        }
        return headers;
    }
}
