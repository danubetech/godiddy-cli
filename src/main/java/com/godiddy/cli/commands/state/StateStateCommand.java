package com.godiddy.cli.commands.state;

import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.state.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "state",
        description = "Return the current state.",
        mixinStandardHelpOptions = true
)
public class StateStateCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateStateCommand.class);

    @Override
    public Integer call() throws Exception {
        Object state = State.getState();
        Api.print(state);
        return 0;
    }
}
