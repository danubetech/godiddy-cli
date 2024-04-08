package com.godiddy.cli.commands.state;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;
import com.danubetech.uniregistrar.clientstateinterface.ClientStateInterface;
import com.danubetech.uniregistrar.local.extensions.handlers.action.HandleActionState;
import com.danubetech.uniregistrar.local.extensions.handlers.action.HandleStateUpdateVerificationMethods;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretJsonWebKeys;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretVerificationMethods;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateUpdateTempKeys;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.api.client.openapi.model.RegistrarState;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import com.godiddy.cli.commands.state.cliinterfaces.CLIClientKeyInterface;
import com.godiddy.cli.commands.state.cliinterfaces.CLIClientStateInterface;
import com.godiddy.cli.util.MappingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "prepare-next",
        description = "Handle the previous request and current state, and prepare the next request.",
        mixinStandardHelpOptions = true
)
public class StatePrepareNextCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StatePrepareNextCommand.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Integer call() throws Exception {

        // load state and next request

        RegistrarState state = (RegistrarState) CLIState.getState();
        if (state == null) {
            System.err.println("No current state for preparing next command.");
            return 1;
        }
        RegistrarRequest prevRequest = (RegistrarRequest) CLIState.getPrev();
        if (prevRequest == null) {
            System.err.println("No previous request for preparing next command.");
            return 1;
        }

        // prepare next request

        ClientKeyInterface clientKeyInterface = new CLIClientKeyInterface();
        ClientStateInterface clientStateInterface = new CLIClientStateInterface();

        RegistrarRequest nextRequest = objectMapper.convertValue(objectMapper.convertValue(prevRequest, Map.class), prevRequest.getClass());

        // handle

        uniregistrar.openapi.model.RegistrarState handleState = MappingUtil.map(state);
        uniregistrar.openapi.model.RegistrarRequest handlePrevRequest = MappingUtil.map(prevRequest);

        HandleFinishedStateImportSecretJsonWebKeys.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleFinishedStateImportSecretVerificationMethods.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleStateUpdateVerificationMethods.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleFinishedStateUpdateTempKeys.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);

        uniregistrar.openapi.model.RegistrarRequest handleNextRequest = MappingUtil.map(nextRequest);

        HandleActionState.handleActionState(handlePrevRequest, handleState, handleNextRequest, clientKeyInterface, clientStateInterface);

        nextRequest = MappingUtil.map(handleNextRequest);

        // save and print next request

        CLIState.setNext(nextRequest);
        Api.print(nextRequest);

        // done

        return 0;
    }
}
