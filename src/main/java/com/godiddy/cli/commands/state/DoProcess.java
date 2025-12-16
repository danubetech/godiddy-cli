package com.godiddy.cli.commands.state;

import com.danubetech.kms.clientkeyinterface.ClientKeyInterface;
import com.danubetech.kms.clientstateinterface.ClientStateInterface;
import com.danubetech.uniregistrar.local.internalsecretmode.InternalSecretMode;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.api.client.openapi.model.RegistrarResourceState;
import com.godiddy.api.client.openapi.model.RegistrarState;
import com.godiddy.cli.clistorage.clistate.CLIState;
import com.godiddy.cli.commands.registrar.DoContinue;
import com.godiddy.cli.config.Api;
import com.godiddy.cli.interfaces.Interfaces;
import com.godiddy.cli.util.MappingUtil;

import java.util.Optional;

public class DoProcess {

    public static Integer doProcess(boolean interactive) throws Exception {

        // load state and next request

        Object state = CLIState.getState();
        if (state == null) {
            System.err.println("No current state for preparing next command.");
            return 1;
        }
        RegistrarRequest request = CLIState.getPrevRequest();
        if (request == null) {
            System.err.println("No previous request for preparing next command.");
            return 1;
        }

        // instantiate client interfaces

        ClientKeyInterface<?> clientKeyInterface = Interfaces.instantiateClientKeyInterface();
        ClientStateInterface clientStateInterface = Interfaces.instantiateClientStateInterface();

        // handle

        RegistrarRequest nextRequest = null;

        if (state instanceof RegistrarState registrarState) {
            uniregistrar.openapi.model.RegistrarRequest mappedRequest = MappingUtil.map(request);
            uniregistrar.openapi.model.RegistrarState mappedState = MappingUtil.map(registrarState);

            Optional<uniregistrar.openapi.model.RegistrarRequest> handleStateResult = InternalSecretMode.handleState(mappedRequest, mappedState, clientKeyInterface, clientStateInterface);
            uniregistrar.openapi.model.RegistrarRequest mappedNextRequest = handleStateResult == null ? null : handleStateResult.orElse(null);

            nextRequest = MappingUtil.map(mappedNextRequest);
        } else if (state instanceof RegistrarResourceState registrarResourceState) {
            uniregistrar.openapi.model.RegistrarRequest mappedRequest = MappingUtil.map(request);
            uniregistrar.openapi.model.RegistrarResourceState mappedState = MappingUtil.map(registrarResourceState);

            Optional<uniregistrar.openapi.model.RegistrarRequest> handleStateResult = InternalSecretMode.handleState(mappedRequest, mappedState, clientKeyInterface, clientStateInterface);
            uniregistrar.openapi.model.RegistrarRequest mappedNextRequest = handleStateResult == null ? null : handleStateResult.orElse(null);

            nextRequest = MappingUtil.map(mappedNextRequest);
        }

        // save next request

        CLIState.setNextRequest(nextRequest);

        // state not handled?

        if (nextRequest == null) {
            return 0;
        }

        // interactive?

        if (interactive) {
            Api.print(nextRequest);
            System.out.println();
            System.out.println("Interactive mode on. Execute 'godiddy-cli continue' to send the next request, or execute 'godiddy-cli state edit-next' to edit it.");
            return 0;
        }

        // continue

        return DoContinue.doContinue(interactive);
    }
}
