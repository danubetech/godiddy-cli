package com.danubetech.did.cli.commands.info;

import com.danubetech.did.cli.DIDAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "resolver",
        description = "Information about the Universal Resolver API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                InfoResolverPropertiesCommand.class,
                InfoResolverMethodsCommand.class,
                InfoResolverTestIdentifiersCommand.class
        }
)
public class InfoResolverCommand extends DIDAbstractCommand implements Callable<Integer> {
}
