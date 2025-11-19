package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.*;
import com.godiddy.cli.clistorage.clistate.CLIState;
import com.godiddy.cli.commands.state.DoProcess;
import com.godiddy.cli.commands.state.StateProcessCommand;
import com.godiddy.cli.config.Api;

public class DoContinue {

    public static Integer doContinue(boolean interactive) throws Exception {

        // request and execute

        String method = CLIState.getMethod();
        RegistrarRequest nextRequest = CLIState.getNextRequest();
        if (nextRequest == null) {
            System.err.println("No next request to continue with. Try running \"godiddy-cli state process\" first.");
            return 1;
        }

        Object state;
        switch (nextRequest) {
            case CreateRequest createRequest -> state = Api.execute(() -> Api.universalRegistrarApi().createWithHttpInfo(method, createRequest));
            case UpdateRequest updateRequest -> state = Api.execute(() -> Api.universalRegistrarApi().updateWithHttpInfo(updateRequest));
            case DeactivateRequest deactivateRequest -> state = Api.execute(() -> Api.universalRegistrarApi().deactivateWithHttpInfo(deactivateRequest));
            case ExecuteRequest executeRequest -> state = Api.execute(() -> Api.universalRegistrarApi().executeWithHttpInfo(executeRequest));
            case CreateResourceRequest createResourceRequest -> state = Api.execute(() -> Api.universalRegistrarApi().createResourceWithHttpInfo(createResourceRequest));
            case UpdateResourceRequest updateResourceRequest -> state = Api.execute(() -> Api.universalRegistrarApi().updateResourceWithHttpInfo(updateResourceRequest));
            case DeactivateResourceRequest deactivateResourceRequest -> state = Api.execute(() -> Api.universalRegistrarApi().deactivateResourceWithHttpInfo(deactivateResourceRequest));
            default -> throw new IllegalStateException("Unexpected request class: " + nextRequest.getClass().getName());
        }

        // store state

        CLIState.setMethod(method);
        CLIState.setState(state);
        CLIState.setPrevRequest(nextRequest);
        CLIState.setNextRequest(null);

        // interactive?

        if (interactive) {
            System.out.println();
            System.out.println("Interactive mode on. Execute 'godiddy-cli state process' to process the state and prepare the next request.");
            return 0;
        }

        // process state

        return DoProcess.doProcess(interactive);
    }
}
