package com.godiddy.cli.commands.state;

import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistorage.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "next",
        description = "Return the next request in the current state.",
        mixinStandardHelpOptions = true
)
public class StateNextCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateNextCommand.class);

    @CommandLine.Option(
            names = {"-o", "--object"},
            description = "Also print in object notation, in addition to JSON.",
            defaultValue = "false"
    )
    Boolean objectNotation;

    @Override
    public Integer call() throws Exception {
        RegistrarRequest nextRequest = CLIState.getNextRequest();
        Api.print(nextRequest);
        if (Boolean.TRUE == this.objectNotation) System.out.println(nextRequest);
        return 0;
    }
}
