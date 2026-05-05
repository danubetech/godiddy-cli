package com.danubetech.did.cli.commands.state;

import com.danubetech.did.cli.DIDAbstractCommand;
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
                StateProcessCommand.class,
                StateEditNextCommand.class,
                StateResetCommand.class
        }
)
public class StateRootCommand extends DIDAbstractCommand implements Callable<Integer> {
}
