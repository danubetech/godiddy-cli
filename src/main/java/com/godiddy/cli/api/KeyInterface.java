package com.godiddy.cli.api;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class KeyInterface {

    private static final Logger log = LogManager.getLogger(KeyInterface.class);

    public enum Value {
        dummy,
        wallet,
        local,
        def
    };

    public static final String DEFAULT_KEYINTERFACE = "local";
    public static final Map<String, Value> PREDEFINED_KEYINTERFACE = Map.of(
            "def", Value.valueOf(DEFAULT_KEYINTERFACE)
    );

    public static Value getKeyInterface() {
        Value keyInterface = Objects.requireNonNullElse(CLIConfig.getKeyInterface(), Value.valueOf(DEFAULT_KEYINTERFACE));
        if (Value.valueOf(DEFAULT_KEYINTERFACE).equals(keyInterface)) {
            log.debug("Using default key interface: " + DEFAULT_KEYINTERFACE);
        }
        return keyInterface;
    }
}
