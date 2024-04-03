package com.godiddy.cli.commands.state;

import com.godiddy.api.client.swagger.model.*;
import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.state.State;
import com.godiddy.cli.util.RequestWrapper;
import com.godiddy.cli.util.StateWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "prepare-next",
        description = "Handle the previous request and current state, and prepare the next request.",
        mixinStandardHelpOptions = true
)
public class StatePrepareNextCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StatePrepareNextCommand.class);

    @Override
    public Integer call() throws Exception {
        Object state = State.getState();
        if (state == null) {
            System.err.println("No current state to handle.");
            return 1;
        }
        Object prev = State.getPrev();
        if (prev == null) {
            System.err.println("No previous request to handle.");
            return 1;
        }
        StateWrapper stateWrapper = new StateWrapper(state);
        RequestWrapper prevRequestWrapper = new RequestWrapper(prev);
        RequestWrapper nextRequestWrapper;
        switch (prev) {
            case CreateRequest prevCreateRequest -> {
                CreateRequest nextCreateRequest = new CreateRequest();
                nextRequestWrapper = new RequestWrapper(nextCreateRequest);
                nextCreateRequest.setDidDocument(prevCreateRequest.getDidDocument());
            }
            case UpdateRequest prevUpdateRequest -> {
                UpdateRequest nextUpdateRequest = new UpdateRequest();
                nextRequestWrapper = new RequestWrapper(nextUpdateRequest);
                nextUpdateRequest.setDidDocument(prevUpdateRequest.getDidDocument());
            }
            case DeactivateRequest prevDeactivateRequest -> {
                DeactivateRequest nextDeactivateRequest = new DeactivateRequest();
                nextRequestWrapper = new RequestWrapper(nextDeactivateRequest);
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }
        nextRequestWrapper.setJobId(stateWrapper.getJobId());
        nextRequestWrapper.setOptions(prevRequestWrapper.getOptions());
        nextRequestWrapper.setSecret(prevRequestWrapper.getSecret());
        State.setNext(nextRequestWrapper.getWrappedRequest());
        Api.print(nextRequestWrapper.getWrappedRequest());
        return 0;
    }
}
