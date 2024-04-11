package com.godiddy.cli.commands.state;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "state",
        description = "Show and handle state of ongoing Universal Registrar jobs.",
        mixinStandardHelpOptions = true,
        subcommands = {
                StateStateCommand.class,
                StatePrevCommand.class,
                StateNextCommand.class,
                StatePrepareNextCommand.class,
                StateEditNextCommand.class,
                StateResetCommand.class
        }
)
public class StateRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
