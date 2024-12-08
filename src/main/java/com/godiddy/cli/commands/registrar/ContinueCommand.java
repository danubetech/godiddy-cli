package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.*;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
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
            description = "This enables interactive mode where the request is prepared but not executed. You can then either run \"godiddy-cli state edit-next\" or \"godiddy-cli state continue-next\"."
    )
    Boolean interactive;

    @Override
    public Integer call() throws Exception {

        // interactive?

        boolean interactive = Boolean.TRUE.equals(this.interactive);

        // continue

        return doContinue(interactive);
    }

    public static Integer doContinue(boolean interactive) throws Exception {

        // request and execute

        String method = CLIState.getMethod();
        RegistrarRequest nextRequest = CLIState.getNextRequest();
        if (nextRequest == null) {
            System.err.println("No next request to continue with. Try running \"godiddy-cli state process\" first.");
            return 1;
        }

        RegistrarState state;
        switch (nextRequest) {
            case CreateRequest createRequest -> state = Api.execute(() -> Api.universalRegistrarApi().createWithHttpInfo(method, createRequest));
            case UpdateRequest updateRequest -> state = Api.execute(() -> Api.universalRegistrarApi().updateWithHttpInfo(updateRequest));
            case DeactivateRequest deactivateRequest -> state = Api.execute(() -> Api.universalRegistrarApi().deactivateWithHttpInfo(deactivateRequest));
            case ExecuteRequest executeRequest -> state = Api.execute(() -> Api.universalRegistrarApi().executeWithHttpInfo(executeRequest));
            default -> throw new IllegalStateException("Unexpected request class: " + nextRequest.getClass().getName());
        }

        // store state

        CLIState.setMethod(method);
        CLIState.setState(state);
        CLIState.setPrevRequest(nextRequest);
        CLIState.setNextRequest(null);

        // interactive?

        if (interactive) {
            Api.print(state);
            System.out.println();
            System.out.println("Interactive mode on. Execute 'godiddy-cli state process' to process the state and prepare the next request.");
            return 0;
        }

        // process state

        return StateProcessCommand.doProcess(interactive);
    }
}
