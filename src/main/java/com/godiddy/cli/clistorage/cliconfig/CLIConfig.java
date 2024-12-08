package com.godiddy.cli.clistorage.cliconfig;

import com.godiddy.cli.api.Formatting;
import com.godiddy.cli.api.Headers;
import com.godiddy.cli.api.KeyInterface;
import com.godiddy.cli.clistorage.CLIStorage;

public class CLIConfig {

    public static String getApiKey() {
        return CLIStorage.get("apiKey");
    }

    public static void setApiKey(String apiKey) {
        if (apiKey == null) {
            CLIStorage.remove("apiKey");
        } else {
            CLIStorage.put("apiKey", apiKey);
        }
    }

    public static String getEndpoint() {
        return CLIStorage.get("endpoint");
    }

    public static void setEndpoint(String endpoint) {
        if (endpoint == null) {
            CLIStorage.remove("endpoint");
        } else {
            CLIStorage.put("endpoint", endpoint);
        }
    }

    public static KeyInterface.Value getKeyInterface() {
        return CLIStorage.get("keyInterface") == null ? null : KeyInterface.Value.valueOf(CLIStorage.get("keyInterface"));
    }

    public static void setKeyInterface(KeyInterface.Value keyInterface) {
        if (keyInterface == null) {
            CLIStorage.remove("keyInterface");
        } else {
            CLIStorage.put("keyInterface", keyInterface.name());
        }
    }

    public static String getWalletServiceBase() {
        return CLIStorage.get("walletServiceBase");
    }

    public static void setWalletServiceBase(String walletServiceBase) {
        if (walletServiceBase == null) {
            CLIStorage.remove("walletServiceBase");
        } else {
            CLIStorage.put("walletServiceBase", walletServiceBase);
        }
    }

    public static Formatting.Value getFormatting() {
        return CLIStorage.get("formatting") == null ? null : Formatting.Value.valueOf(CLIStorage.get("formatting"));
    }

    public static void setFormatting(Formatting.Value formatting) {
        if (formatting == null) {
            CLIStorage.remove("formatting");
        } else {
            CLIStorage.put("formatting", formatting.name());
        }
    }

    public static Headers.Value getHeaders() {
        return CLIStorage.get("headers") == null ? null : Headers.Value.valueOf(CLIStorage.get("headers"));
    }

    public static void setHeaders(Headers.Value headers) {
        if (headers == null) {
            CLIStorage.remove("headers");
        } else {
            CLIStorage.put("headers", headers.name());
        }
    }
}
