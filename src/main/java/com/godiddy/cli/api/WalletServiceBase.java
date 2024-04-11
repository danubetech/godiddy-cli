package com.godiddy.cli.api;

import com.godiddy.cli.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class WalletServiceBase {

    private static final Logger log = LogManager.getLogger(WalletServiceBase.class);

    public static final String DEFAULT_WALLETSERVICEBASE = "http://localhost:12080/wallet-service/1.0.0";
    public static final Map<String, String> PREDEFINED_WALLETSERVICEBASES = Map.of(
            "def", DEFAULT_WALLETSERVICEBASE
    );

    public static String getWalletServiceBase() {
        String walletServiceBase = Objects.requireNonNullElse(CLIConfig.getWalletServiceBase(), DEFAULT_WALLETSERVICEBASE);
        if (DEFAULT_WALLETSERVICEBASE.equals(walletServiceBase)) {
            log.info("Using default wallet service base: " + DEFAULT_WALLETSERVICEBASE);
        }
        return walletServiceBase;
    }
}
