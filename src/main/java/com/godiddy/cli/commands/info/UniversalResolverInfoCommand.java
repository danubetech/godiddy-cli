package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "resolver",
        description = "Information about the Universal Resolver in the Godiddy API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                UniversalResolverPropertiesCommand.class,
                UniversalResolverMethodsCommand.class,
                UniversalResolverTestIdentifiersCommand.class
        }
)
public class UniversalResolverInfoCommand extends GodiddyCommand implements Callable<Integer> {
}
