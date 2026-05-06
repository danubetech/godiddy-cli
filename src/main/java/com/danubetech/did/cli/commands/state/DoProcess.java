package com.danubetech.did.cli.commands.state;

import com.danubetech.did.api.client.openapi.model.DidState;
import com.danubetech.did.api.client.openapi.model.RegistrarRequest;
import com.danubetech.did.api.client.openapi.model.RegistrarResourceState;
import com.danubetech.did.api.client.openapi.model.RegistrarState;
import com.danubetech.did.cli.clistorage.clistate.CLIState;
import com.danubetech.did.cli.commands.registrar.DoContinue;
import com.danubetech.did.cli.config.Api;
import com.danubetech.did.cli.interfaces.Interfaces;
import com.danubetech.did.cli.util.MappingUtil;
import com.danubetech.kms.clientkeyinterface.ClientKeyInterface;
import com.danubetech.kms.clientstateinterface.ClientStateInterface;
import com.danubetech.uniregistrar.local.internalsecretmode.InternalSecretMode;

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
        String didState = null;

        if (state instanceof RegistrarState registrarState) {
            uniregistrar.openapi.model.RegistrarRequest mappedRequest = MappingUtil.map(request);
            uniregistrar.openapi.model.RegistrarState mappedState = MappingUtil.map(registrarState);

            Optional<uniregistrar.openapi.model.RegistrarRequest> handleStateResult = InternalSecretMode.handleState(mappedRequest, mappedState, clientKeyInterface, clientStateInterface);
            uniregistrar.openapi.model.RegistrarRequest mappedNextRequest = handleStateResult == null ? null : handleStateResult.orElse(null);

            nextRequest = MappingUtil.map(mappedNextRequest);
            didState = registrarState.getDidState() == null ? null : registrarState.getDidState().getState();
        } else if (state instanceof RegistrarResourceState registrarResourceState) {
            uniregistrar.openapi.model.RegistrarRequest mappedRequest = MappingUtil.map(request);
            uniregistrar.openapi.model.RegistrarResourceState mappedState = MappingUtil.map(registrarResourceState);

            Optional<uniregistrar.openapi.model.RegistrarRequest> handleStateResult = InternalSecretMode.handleState(mappedRequest, mappedState, clientKeyInterface, clientStateInterface);
            uniregistrar.openapi.model.RegistrarRequest mappedNextRequest = handleStateResult == null ? null : handleStateResult.orElse(null);

            nextRequest = MappingUtil.map(mappedNextRequest);
            didState = registrarResourceState.getDidUrlState() == null ? null : registrarResourceState.getDidUrlState().getState();
        }

        // save next request

        if ("finished".equals(didState) || "failed".equals(didState)) {
            CLIState.setNextRequest(null);
        } else if (nextRequest != null) {
            CLIState.setNextRequest(nextRequest);
        } else {
            CLIState.setNextRequest(request);
        }

        // state not handled?

        if (nextRequest == null) {
            return 0;
        }

        // interactive?

        if (interactive) {
            Api.print(nextRequest);
            System.out.println();
            System.out.println("Interactive mode on. Execute 'did-cli continue' to send the next request, or execute 'did-cli state edit-next' to edit it.");
            return 0;
        }

        // continue

        return DoContinue.doContinue(interactive);
    }
}
