package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "info",
        description = "Various information about the Godiddy API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                UniversalResolverInfoCommand.class,
                UniversalRegistrarInfoCommand.class
        }
)
public class InfoCommand extends GodiddyCommand implements Callable<Integer> {
}
