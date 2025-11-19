package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class VaultToken {

    private static final Logger log = LogManager.getLogger(VaultToken.class);

    public static final String DEFAULT_VAULTTOKEN = "";
    public static final Map<String, String> PREDEFINED_VAULTTOKENS = Map.of(
            "def", DEFAULT_VAULTTOKEN
    );

    public static String getVaultToken() {
        String vaultEndpoint = Objects.requireNonNullElse(CLIConfig.getVaultToken(), DEFAULT_VAULTTOKEN);
        if (DEFAULT_VAULTTOKEN.equals(vaultEndpoint)) {
            log.debug("Using default vault token: " + DEFAULT_VAULTTOKEN);
        }
        return vaultEndpoint;
    }
}
