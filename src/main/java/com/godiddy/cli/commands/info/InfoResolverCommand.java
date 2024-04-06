package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "resolver",
        description = "Information about the Universal Resolver in the Godiddy API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                InfoResolverPropertiesCommand.class,
                InfoResolverMethodsCommand.class,
                InfoResolverTestIdentifiersCommand.class
        }
)
public class InfoResolverCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
