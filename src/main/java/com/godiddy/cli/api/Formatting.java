package com.godiddy.cli.api;

import com.godiddy.cli.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class Formatting {

    private static final Logger log = LogManager.getLogger(Formatting.class);

    public enum Value {
        pretty,
        flat,
        raw,
        off,
        def
    };

    public static final String DEFAULT_FORMATTING = "pretty";
    public static final Map<String, Value> PREDEFINED_FORMATTINGS = Map.of(
            "def", Value.valueOf(DEFAULT_FORMATTING)
    );

    public static Value getFormatting() {
        Value formatting = Objects.requireNonNullElse(Configuration.getFormatting(), Value.valueOf(DEFAULT_FORMATTING));
        if (Value.valueOf(DEFAULT_FORMATTING).equals(formatting)) {
            log.info("Using default formatting: " + DEFAULT_FORMATTING);
        }
        return formatting;
    }
}