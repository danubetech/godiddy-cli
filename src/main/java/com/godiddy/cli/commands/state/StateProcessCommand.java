package com.godiddy.cli.commands.state;

import com.godiddy.cli.GodiddyAbstractCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "process",
        description = "Process the previous request and current state, and prepare the next request.",
        mixinStandardHelpOptions = true
)
public class StateProcessCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateProcessCommand.class);

    @Override
    public Integer call() throws Exception {

        return DoProcess.doProcess(true);
    }
}
