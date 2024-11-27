package com.godiddy.cli.clidata.cliconfig;

import com.godiddy.cli.api.Formatting;
import com.godiddy.cli.api.Headers;
import com.godiddy.cli.api.KeyInterface;
import com.godiddy.cli.clidata.CLIData;

public class CLIConfig {

    public static String getApiKey() {
        return CLIData.get("apiKey");
    }

    public static void setApiKey(String apiKey) {
        if (apiKey == null) {
            CLIData.remove("apiKey");
        } else {
            CLIData.put("apiKey", apiKey);
        }
    }

    public static String getEndpoint() {
        return CLIData.get("endpoint");
    }

    public static void setEndpoint(String endpoint) {
        if (endpoint == null) {
            CLIData.remove("endpoint");
        } else {
            CLIData.put("endpoint", endpoint);
        }
    }

    public static KeyInterface.Value getKeyInterface() {
        return CLIData.get("keyInterface") == null ? null : KeyInterface.Value.valueOf(CLIData.get("keyInterface"));
    }

    public static void setKeyInterface(KeyInterface.Value keyInterface) {
        if (keyInterface == null) {
            CLIData.remove("keyInterface");
        } else {
            CLIData.put("keyInterface", keyInterface.name());
        }
    }

    public static String getWalletServiceBase() {
        return CLIData.get("walletServiceBase");
    }

    public static void setWalletServiceBase(String walletServiceBase) {
        if (walletServiceBase == null) {
            CLIData.remove("walletServiceBase");
        } else {
            CLIData.put("walletServiceBase", walletServiceBase);
        }
    }

    public static Formatting.Value getFormatting() {
        return CLIData.get("formatting") == null ? null : Formatting.Value.valueOf(CLIData.get("formatting"));
    }

    public static void setFormatting(Formatting.Value formatting) {
        if (formatting == null) {
            CLIData.remove("formatting");
        } else {
            CLIData.put("formatting", formatting.name());
        }
    }

    public static Headers.Value getHeaders() {
        return CLIData.get("headers") == null ? null : Headers.Value.valueOf(CLIData.get("headers"));
    }

    public static void setHeaders(Headers.Value headers) {
        if (headers == null) {
            CLIData.remove("headers");
        } else {
            CLIData.put("headers", headers.name());
        }
    }
}
