package com.danubetech.did.cli.commands.info;

import com.danubetech.did.cli.DIDAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "info",
        description = "Various information about the API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                InfoResolverCommand.class,
                InfoRegistrarCommand.class
        }
)
public class InfoRootCommand extends DIDAbstractCommand implements Callable<Integer> {
}
