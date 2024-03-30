package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "registrar",
        description = "Information about the Universal Registrar in the Godiddy API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                UniversalRegistrarPropertiesCommand.class,
                UniversalRegistrarMethodsCommand.class
        }
)
public class UniversalRegistrarInfoCommand extends GodiddyCommand implements Callable<Integer> {
}
