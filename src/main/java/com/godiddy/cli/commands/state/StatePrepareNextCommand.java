package com.godiddy.cli.commands.state;

import com.godiddy.api.client.openapi.model.*;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.commands.state.prepare.HandleActionStateExtension;
import com.godiddy.cli.clistate.CLIState;
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
public class StatePrepareNextCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StatePrepareNextCommand.class);

    @Override
    public Integer call() throws Exception {

        // load state and next request

        Object state = CLIState.getState();
        if (state == null) {
            System.err.println("No current state for preparing next command.");
            return 1;
        }
        Object prev = CLIState.getPrev();
        if (prev == null) {
            System.err.println("No previous request for preparing next command.");
            return 1;
        }

        // prepare next request

        StateWrapper stateWrapper = new StateWrapper(state);
        RegistrarRequest prevRequest = (RegistrarRequest) prev;
        RegistrarRequest nextRequest;

        switch (prev) {
            case CreateRequest ignored -> nextRequest = new CreateRequest();
            case UpdateRequest ignored -> nextRequest = new UpdateRequest();
            case DeactivateRequest ignored -> nextRequest = new DeactivateRequest();
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }

        // prepare next request - jobId

        nextRequest.setJobId(stateWrapper.getJobId());

        // prepare next request - options

        nextRequest.setOptions(prevRequest.getOptions());

        // prepare next request - secret

        nextRequest.setSecret(prevRequest.getSecret());

        // prepare next request - did

        switch (prev) {
            case CreateRequest prevCreateRequest -> {
            }
            case UpdateRequest prevUpdateRequest -> {
                ((UpdateRequest) nextRequest).setDid(prevUpdateRequest.getDid());
            }
            case DeactivateRequest prevDeactivateRequest -> {
                ((DeactivateRequest) nextRequest).setDid(prevDeactivateRequest.getDid());
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }

        // prepare next request - didDocumentOperation

        switch (prev) {
            case CreateRequest prevCreateRequest -> {
            }
            case UpdateRequest prevUpdateRequest -> {
                ((UpdateRequest) nextRequest).setDidDocumentOperation(prevUpdateRequest.getDidDocumentOperation());
            }
            case DeactivateRequest prevDeactivateRequest -> {
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }

        // prepare next request - didDocument

        switch (prev) {
            case CreateRequest prevCreateRequest -> {
                ((CreateRequest) nextRequest).setDidDocument(prevCreateRequest.getDidDocument());
            }
            case UpdateRequest prevUpdateRequest -> {
                ((UpdateRequest) nextRequest).setDidDocument(prevUpdateRequest.getDidDocument());
            }
            case DeactivateRequest prevDeactivateRequest -> {
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }

        // prepare next request - didState.action="getVerificationMethod"

/*        if ("getVerificationMethod".equals(stateWrapper.getDidState().getState())) {
            new HandleActionStateExtension().handleGetVerificationAction(stateWrapper.getDidState(), null);
        }*/

        // save and print next request

        CLIState.setNext(nextRequest);
        Api.print(nextRequest);

        // done

        return 0;
    }
}
