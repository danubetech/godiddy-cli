package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.*;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.config.Api;
import com.godiddy.cli.clistorage.clistate.CLIState;
import com.godiddy.cli.commands.state.StateProcessCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "continue",
        description = "Continue an ongoing DID operation, using the Universal Registrar API.",
        mixinStandardHelpOptions = true
)
public class ContinueCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ContinueCommand.class);

    @CommandLine.Option(
            names = {"-i", "--interactive"},
            description = "This enables interactive mode where the request is prepared but not executed. You can then either run \"godiddy-cli state edit-next\" or \"godiddy-cli state continue\"."
    )
    Boolean interactive;

    @Override
    public Integer call() throws Exception {

        // interactive?

        boolean interactive = Boolean.TRUE.equals(this.interactive);

        // continue

        return DoContinue.doContinue(interactive);
    }
}
