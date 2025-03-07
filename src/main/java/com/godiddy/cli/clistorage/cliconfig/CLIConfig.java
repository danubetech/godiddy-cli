package com.godiddy.cli.clistorage.cliconfig;

import com.godiddy.cli.config.Formatting;
import com.godiddy.cli.config.Headers;
import com.godiddy.cli.config.Kms;
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

    public static Kms.Value getKms() {
        return CLIStorage.get("kms") == null ? null : Kms.Value.valueOf(CLIStorage.get("kms"));
    }

    public static void setKms(Kms.Value kms) {
        if (kms == null) {
            CLIStorage.remove("kms");
        } else {
            CLIStorage.put("kms", kms.name());
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
