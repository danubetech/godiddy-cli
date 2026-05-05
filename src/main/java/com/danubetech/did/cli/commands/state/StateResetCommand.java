package com.danubetech.did.cli.commands.state;

import com.danubetech.did.cli.DIDAbstractCommand;
import com.danubetech.did.cli.clistorage.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "reset",
        description = "Reset the current state, including method, previous request, state, and next request.",
        mixinStandardHelpOptions = true
)
public class StateResetCommand extends DIDAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateResetCommand.class);

    @Override
    public Integer call() throws Exception {
        CLIState.setMethod(null);
        CLIState.setState(null);
        CLIState.setPrevRequest(null);
        CLIState.setNextRequest(null);
        CLIState.setClientStateObject(null);
        System.out.println("State successfully reset.");
        return 0;
    }
}
