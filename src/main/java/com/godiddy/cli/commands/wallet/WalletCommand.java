package com.godiddy.cli.commands.wallet;

import com.godiddy.cli.GodiddyCommand;
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
public class WalletCommand extends GodiddyCommand implements Callable<Integer> {
}
