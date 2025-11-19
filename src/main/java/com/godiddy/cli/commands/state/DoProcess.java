package com.godiddy.cli.commands.state;

import com.danubetech.kms.clientkeyinterface.ClientKeyInterface;
import com.danubetech.kms.clientstateinterface.ClientStateInterface;
import com.danubetech.uniregistrar.local.extensions.handlers.HandleStateUpdateTempKeys;
import com.danubetech.uniregistrar.local.extensions.handlers.HandleStateUpdateVerificationMethods;
import com.danubetech.uniregistrar.local.extensions.handlers.action.HandleActionState;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretJsonWebKeys;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretVerificationMethods;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.api.client.openapi.model.RegistrarResourceState;
import com.godiddy.api.client.openapi.model.RegistrarState;
import com.godiddy.cli.clistorage.clistate.CLIState;
import com.godiddy.cli.commands.registrar.ContinueCommand;
import com.godiddy.cli.commands.registrar.DoContinue;
import com.godiddy.cli.config.Api;
import com.godiddy.cli.interfaces.Interfaces;
import com.godiddy.cli.util.MappingUtil;

public class DoProcess {

    public static Integer doProcess(boolean interactive) throws Exception {

        // load state and next request

        Object state = CLIState.getState();
        if (state == null) {
            System.err.println("No current state for preparing next command.");
            return 1;
        }
        RegistrarRequest prevRequest = CLIState.getPrevRequest();
        if (prevRequest == null) {
            System.err.println("No previous request for preparing next command.");
            return 1;
        }

        // instantiate client interfaces

        ClientKeyInterface<?> clientKeyInterface = Interfaces.instantiateClientKeyInterface();
        ClientStateInterface clientStateInterface = Interfaces.instantiateClientStateInterface();

        // handle

        RegistrarRequest nextRequest = null;

        if (state instanceof RegistrarState registrarState) {
            uniregistrar.openapi.model.RegistrarState handleState = MappingUtil.map(registrarState);
            uniregistrar.openapi.model.RegistrarRequest handlePrevRequest = MappingUtil.map(prevRequest);

            HandleStateUpdateVerificationMethods.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            HandleStateUpdateTempKeys.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            HandleFinishedStateImportSecretJsonWebKeys.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            HandleFinishedStateImportSecretVerificationMethods.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            uniregistrar.openapi.model.RegistrarRequest handleNextRequest = HandleActionState.handleActionState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);

            nextRequest = MappingUtil.map(handleNextRequest);
        } else if (state instanceof RegistrarResourceState registrarResourceState) {
            uniregistrar.openapi.model.RegistrarResourceState handleState = MappingUtil.map(registrarResourceState);
            uniregistrar.openapi.model.RegistrarRequest handlePrevRequest = MappingUtil.map(prevRequest);

            HandleStateUpdateVerificationMethods.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            HandleStateUpdateTempKeys.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            HandleFinishedStateImportSecretJsonWebKeys.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            HandleFinishedStateImportSecretVerificationMethods.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
            uniregistrar.openapi.model.RegistrarRequest handleNextRequest = HandleActionState.handleActionState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);

            nextRequest = MappingUtil.map(handleNextRequest);
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
