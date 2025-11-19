package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class VaultEndpoint {

    private static final Logger log = LogManager.getLogger(VaultEndpoint.class);

    public static final String DEFAULT_VAULTENDPOINT = "http://localhost:8200/";
    public static final Map<String, String> PREDEFINED_VAULTENDPOINTS = Map.of(
            "def", DEFAULT_VAULTENDPOINT
    );

    public static String getVaultEndpoint() {
        String vaultEndpoint = Objects.requireNonNullElse(CLIConfig.getVaultEndpoint(), DEFAULT_VAULTENDPOINT);
        if (DEFAULT_VAULTENDPOINT.equals(vaultEndpoint)) {
            log.debug("Using default vault endpoint: " + DEFAULT_VAULTENDPOINT);
        }
        return vaultEndpoint;
    }
}
