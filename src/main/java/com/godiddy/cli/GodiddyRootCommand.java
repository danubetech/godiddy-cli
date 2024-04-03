package com.godiddy.cli;

import com.godiddy.cli.commands.config.ConfigRootCommand;
import com.godiddy.cli.commands.info.InfoRootCommand;
import com.godiddy.cli.commands.registrar.CreateCommand;
import com.godiddy.cli.commands.resolver.ResolveCommand;
import com.godiddy.cli.commands.wallet.WalletRootCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "godiddy-cli",
        description = "Godiddy Universal DID Operations",
        footer = "See https://docs.godiddy.com/ for more information.",
        version = "1.0.0",
        mixinStandardHelpOptions = true,
        subcommands = {
                ConfigRootCommand.class,
                InfoRootCommand.class,
                ResolveCommand.class,
                CreateCommand.class,
                WalletRootCommand.class
        }
)
public class GodiddyRootCommand extends GodiddyCommand implements Callable<Integer> {
}
