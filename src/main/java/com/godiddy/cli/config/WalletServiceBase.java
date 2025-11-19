package com.godiddy.cli.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;


public class WalletServiceBase {

    private static final Logger log = LogManager.getLogger(WalletServiceBase.class);

    public static final String DEFAULT_WALLETSERVICEBASE = "http://localhost:12080/wallet-service/1.0.0";
    public static final Map<String, String> PREDEFINED_WALLETSERVICEBASES = Map.of(
            "def", DEFAULT_WALLETSERVICEBASE,
            "godiddy", "https://api.godiddy.com/1.0.0/wallet-service/",
            "godiddy-dev", "https://api.dev.godiddy.com/1.0.0/wallet-service/",
            "godiddy-1.0.0", "https://api.godiddy.com/1.0.0/wallet-service/",
            "godiddy-dev-1.0.0", "https://api.dev.godiddy.com/1.0.0/wallet-service/",
            "godiddy-0.1.0", "https://api.godiddy.com/0.1.0/wallet-service/",
            "godiddy-dev-0.1.0", "https://api.dev.godiddy.com/0.1.0/wallet-service/"
    );

    public static String getWalletServiceBase() {
        String walletServiceBase = Objects.requireNonNullElse(CLIConfig.getWalletServiceBase(), DEFAULT_WALLETSERVICEBASE);
        if (DEFAULT_WALLETSERVICEBASE.equals(walletServiceBase)) {
            log.debug("Using default wallet service base: " + DEFAULT_WALLETSERVICEBASE);
        }
        return walletServiceBase;
    }
}
