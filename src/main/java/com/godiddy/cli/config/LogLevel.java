package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;

public class LogLevel {

    private static final Logger log = LogManager.getLogger(LogLevel.class);

    public enum Value {
        warn,
        standard,
        info,
        debug,
        trace,
        def
    };

    public static final String DEFAULT_LOGLEVEL = "standard";
    public static final Map<String, Value> PREDEFINED_LOGLEVEL = Map.of(
            "def", Value.valueOf(DEFAULT_LOGLEVEL)
    );

    public static Value getLog() {
        Value logLevel = Objects.requireNonNullElse(CLIConfig.getLogLevel(), Value.valueOf(DEFAULT_LOGLEVEL));
        if (Value.valueOf(DEFAULT_LOGLEVEL).equals(logLevel)) {
            log.info("Using default log level: " + DEFAULT_LOGLEVEL);
        }
        return logLevel;
    }
}
