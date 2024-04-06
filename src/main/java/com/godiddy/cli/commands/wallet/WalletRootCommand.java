package com.godiddy.cli.commands.wallet;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "wallet",
        description = "Wallet Service commands.",
        mixinStandardHelpOptions = true,
        subcommands = {
                WalletGetKeysCommand.class,
                WalletGetControllersCommand.class
        }
)
public class WalletRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
