package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.*;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "continue",
        description = "Continue an ongoing DID operation, using the Universal Registrar API.",
        mixinStandardHelpOptions = true
)
public class ContinueCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ContinueCommand.class);

    @Override
    public Integer call() throws Exception {

        // request and execute

        String method = CLIState.getMethod();
        RegistrarRequest nextRequest = CLIState.getNextRequest();
        if (nextRequest == null) {
            System.err.println("No next request to continue with. Try running \"godiddy-cli state prepare-next\" first.");
            return 1;
        }

        RegistrarState state;
        switch (nextRequest) {
            case CreateRequest createRequest -> state = Api.execute(() -> Api.universalRegistrarApi().createWithHttpInfo(method, createRequest));
            case UpdateRequest updateRequest -> state = Api.execute(() -> Api.universalRegistrarApi().updateWithHttpInfo(updateRequest));
            case DeactivateRequest deactivateRequest -> state = Api.execute(() -> Api.universalRegistrarApi().deactivateWithHttpInfo(deactivateRequest));
            default -> throw new IllegalStateException("Unexpected request class: " + nextRequest.getClass().getName());
        }

        // handle state

        if (state.getDidState() instanceof DidStateFinished) {
            CLIState.setMethod(null);
            CLIState.setState(null);
            CLIState.setPrevRequest(null);
            CLIState.setNextRequest(null);
        } else {
            CLIState.setMethod(method);
            CLIState.setState(state);
            CLIState.setPrevRequest(nextRequest);
            CLIState.setNextRequest(null);
        }

        // done

        return 0;
    }
}
