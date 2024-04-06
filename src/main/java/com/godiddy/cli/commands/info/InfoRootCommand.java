package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "info",
        description = "Various information about the Godiddy API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                InfoResolverCommand.class,
                InfoRegistrarCommand.class
        }
)
public class InfoRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
