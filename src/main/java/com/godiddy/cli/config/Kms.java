package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class Kms {

    private static final Logger log = LogManager.getLogger(Kms.class);

    public enum Value {
        dummy,
        walletservice,
        local,
        def
    };

    public static final String DEFAULT_KMS = "local";
    public static final Map<String, Value> PREDEFINED_KMS = Map.of(
            "def", Value.valueOf(DEFAULT_KMS)
    );

    public static Value getKms() {
        Value kms = Objects.requireNonNullElse(CLIConfig.getKms(), Value.valueOf(DEFAULT_KMS));
        if (Value.valueOf(DEFAULT_KMS).equals(kms)) {
            log.debug("Using default KMS: " + DEFAULT_KMS);
        }
        return kms;
    }
}
