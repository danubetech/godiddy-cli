package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "registrar",
        description = "Information about the Universal Registrar in the Godiddy API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                InfoRegistrarPropertiesCommand.class,
                InfoRegistrarMethodsCommand.class
        }
)
public class InfoRegistrarCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
