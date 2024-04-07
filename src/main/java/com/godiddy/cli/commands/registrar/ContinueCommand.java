package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.*;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import com.godiddy.cli.util.StateWrapper;
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
        Object next = CLIState.getNext();
        if (next == null) {
            System.err.println("No next request to continue with. Try running \"godiddy-cli state prepare-next\" first.");
            return 1;
        }
        Object request = next;
        StateWrapper stateWrapper;
        switch (request) {
            case CreateRequest createRequest -> {
                CreateState createState = Api.execute(() -> Api.universalRegistrarApi().createWithHttpInfo(method, createRequest));
                stateWrapper = new StateWrapper(createState);
            }
            case UpdateRequest updateRequest -> {
                UpdateState updateState = Api.execute(() -> Api.universalRegistrarApi().updateWithHttpInfo(updateRequest));
                stateWrapper = new StateWrapper(updateState);
            }
            case DeactivateRequest deactivateRequest -> {
                DeactivateState deactivateState = Api.execute(() -> Api.universalRegistrarApi().deactivateWithHttpInfo(deactivateRequest));
                stateWrapper = new StateWrapper(deactivateState);
            }
            default -> throw new IllegalStateException("Unexpected request class: " + next.getClass().getName());
        }

        // handle state

        if (stateWrapper.getDidState() instanceof DidStateFinished) {
            CLIState.setState(null);
            CLIState.setPrev(null);
            CLIState.setNext(null);
        } else {
            CLIState.setState(stateWrapper.getWrappedState());
            CLIState.setPrev(request);
            CLIState.setNext(null);
        }

        // done

        return 0;
    }
}
