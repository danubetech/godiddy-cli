package com.danubetech.did.cli.commands.state;

import com.danubetech.did.cli.DIDAbstractCommand;
import com.danubetech.did.cli.clistorage.clistate.CLIState;
import com.danubetech.did.cli.config.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "state",
        description = "Return the current state.",
        mixinStandardHelpOptions = true
)
public class StateStateCommand extends DIDAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateStateCommand.class);

    @CommandLine.Option(
            names = {"-o", "--object"},
            description = "Also print in object notation, in addition to JSON.",
            defaultValue = "false"
    )
    Boolean objectNotation;

    @Override
    public Integer call() throws Exception {
        Object state = CLIState.getState();
        Api.print(state);
        if (Boolean.TRUE == this.objectNotation) System.out.println(state);
        return 0;
    }
}
