package com.godiddy.cli.commands.state;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "next",
        description = "Return the next request in the current state.",
        mixinStandardHelpOptions = true
)
public class StateNextCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateNextCommand.class);

    @Override
    public Integer call() throws Exception {
        Object nextRequest = CLIState.getNextRequest();
        Api.print(nextRequest);
        return 0;
    }
}
