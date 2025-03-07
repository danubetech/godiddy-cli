package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class Formatting {

    private static final Logger log = LogManager.getLogger(Formatting.class);

    public enum Value {
        interpreted,
        pretty,
        flat,
        raw,
        off,
        def
    };

    public static final String DEFAULT_FORMATTING = "interpreted";
    public static final Map<String, Value> PREDEFINED_FORMATTINGS = Map.of(
            "def", Value.valueOf(DEFAULT_FORMATTING)
    );

    public static Value getFormatting() {
        Value formatting = Objects.requireNonNullElse(CLIConfig.getFormatting(), Value.valueOf(DEFAULT_FORMATTING));
        if (Value.valueOf(DEFAULT_FORMATTING).equals(formatting)) {
            log.debug("Using default formatting: " + DEFAULT_FORMATTING);
        }
        return formatting;
    }
}
