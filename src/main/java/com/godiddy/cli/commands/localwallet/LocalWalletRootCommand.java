package com.godiddy.cli.commands.localwallet;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "localwallet",
        description = "Local Wallet commands.",
        mixinStandardHelpOptions = true,
        subcommands = {
                LocalWalletShowCommand.class,
        }
)
public class LocalWalletRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
