package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.swagger.model.*;
import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.state.State;
import com.godiddy.cli.util.StateWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(
        name = "continue",
        description = "Continue an ongoing DID operation.",
        mixinStandardHelpOptions = true
)
public class ContinueCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ContinueCommand.class);

    @Option(
            names = {"-m", "--method"},
            description = "The requested DID method for the operation.",
            required = true
    )
    String method;

    @Override
    public Integer call() throws Exception {

        // request and execute

        Object next = State.getNext();
        if (next == null) {
            System.err.println("No next request to continue with. Try running \"godiddy-cli state prepare-next\" first.");
            return 1;
        }
        String method = this.method;
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

        if ("finished".equalsIgnoreCase(stateWrapper.getDidState().getState())) {
            State.setState(null);
            State.setPrev(null);
            State.setNext(null);
        } else {
            State.setState(stateWrapper.getWrappedState());
            State.setPrev(request);
            State.setNext(null);
        }

        // done

        return 0;
    }
}
