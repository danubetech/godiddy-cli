package com.godiddy.cli.commands.state;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "prev",
        description = "Return the previous request in the current state.",
        mixinStandardHelpOptions = true
)
public class StatePrevCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StatePrevCommand.class);

    @Override
    public Integer call() throws Exception {
        Object prev = CLIState.getPrev();
        Api.print(prev);
        return 0;
    }
}
