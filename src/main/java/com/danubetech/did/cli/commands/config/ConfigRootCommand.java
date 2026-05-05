package com.danubetech.did.cli.commands.config;

import com.danubetech.did.cli.DIDAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "config",
        description = "Configuration settings for the DID CLI and API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                ConfigApiKeyCommand.class,
                ConfigEndpointCommand.class,
                ConfigFormattingCommand.class,
                ConfigHeadersCommand.class,
                ConfigKmsCommand.class,
                ConfigLogLevelCommand.class,
                ConfigWalletServiceBaseCommand.class,
                ConfigVaultEndpointCommand.class,
                ConfigVaultTokenCommand.class
        }
)
public class ConfigRootCommand extends DIDAbstractCommand implements Callable<Integer> {
}
