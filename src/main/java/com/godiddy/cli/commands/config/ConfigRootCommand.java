package com.godiddy.cli.commands.config;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "config",
        description = "Configuration settings for the Godiddy CLI and API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                ConfigApiKeyCommand.class,
                ConfigEndpointCommand.class,
                ConfigWalletServiceBaseCommand.class,
                ConfigFormattingCommand.class,
                ConfigHeadersCommand.class
        }
)
public class ConfigRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
