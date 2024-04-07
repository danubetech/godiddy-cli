package com.godiddy.cli.commands.state;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "state",
        description = "Return the current state.",
        mixinStandardHelpOptions = true
)
public class StateStateCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateStateCommand.class);

    @Override
    public Integer call() throws Exception {
        Object state = CLIState.getState();
        Api.print(state);
        return 0;
    }
}
