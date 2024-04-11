package com.godiddy.cli.commands.state;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;
import com.danubetech.uniregistrar.clientstateinterface.ClientStateInterface;
import com.danubetech.uniregistrar.local.extensions.handlers.HandleStateUpdateVerificationMethods;
import com.danubetech.uniregistrar.local.extensions.handlers.action.HandleActionState;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretJsonWebKeys;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretVerificationMethods;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateUpdateTempKeys;
import com.danubetech.walletservice.client.WalletServiceClient;
import com.godiddy.api.client.openapi.model.*;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.api.WalletServiceBase;
import com.godiddy.cli.clistate.CLIState;
import com.godiddy.cli.commands.state.interfaces.CLIStateClientStateInterface;
import com.godiddy.cli.commands.state.interfaces.DummyClientKeyInterface;
import com.godiddy.cli.commands.state.interfaces.WalletServiceClientKeyInterface;
import com.godiddy.cli.util.MappingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
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

    public enum ClientKeyInterfaceType {
        dummy,
        wallet
    };

    public static final String DEFAULT_CLIENTKEYINTERFACETYPE = "dummy";

    @CommandLine.Option(
            names = {"-k", "--keyinterface"},
            description = "The type of client key interface to use. Valid values: ${COMPLETION-CANDIDATES}. Default value: " + DEFAULT_CLIENTKEYINTERFACETYPE + ".",
            arity = "0..1",
            defaultValue = "dummy"
    )
    ClientKeyInterfaceType clientKeyInterfaceType;

    @Override
    public Integer call() throws Exception {

        // load state and next request

        RegistrarState state = CLIState.getState();
        if (state == null) {
            System.err.println("No current state for preparing next command.");
            return 1;
        }
        RegistrarRequest prevRequest = CLIState.getPrevRequest();
        if (prevRequest == null) {
            System.err.println("No previous request for preparing next command.");
            return 1;
        }

        // prepare interfaces

        ClientKeyInterface clientKeyInterface = switch (this.clientKeyInterfaceType) {
            case dummy -> new DummyClientKeyInterface();
            case wallet -> new WalletServiceClientKeyInterface(WalletServiceClient.create(WalletServiceBase.getWalletServiceBase()), "default");
        };
        ClientStateInterface clientStateInterface = new CLIStateClientStateInterface();

        // handle

        uniregistrar.openapi.model.RegistrarState handleState = MappingUtil.map(state);
        uniregistrar.openapi.model.RegistrarRequest handlePrevRequest = MappingUtil.map(prevRequest);

        HandleFinishedStateImportSecretJsonWebKeys.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleFinishedStateImportSecretVerificationMethods.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleStateUpdateVerificationMethods.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleFinishedStateUpdateTempKeys.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);

        // prepare next request

        RegistrarRequest nextRequest = Api.convert(Api.convert(prevRequest, Map.class), prevRequest.getClass());
        if (state.getJobId() != null) nextRequest.setJobId(state.getJobId());
        if (state.getDidState().getDidDocument() != null) {
            switch (nextRequest) {
                case CreateRequest createRequest -> createRequest.setDidDocument(state.getDidState().getDidDocument());
                case UpdateRequest updateRequest -> updateRequest.setDidDocument(updateRequest.getDidDocument().stream().map(x -> state.getDidState().getDidDocument()).toList());
                case DeactivateRequest ignored -> { }
                case null -> throw new NullPointerException();
                default -> throw new IllegalArgumentException("Invalid request type: " + nextRequest.getClass().getSimpleName());
            }
        }

        // handle

        uniregistrar.openapi.model.RegistrarRequest handleNextRequest = MappingUtil.map(nextRequest);
        HandleActionState.handleActionState(handlePrevRequest, handleState, handleNextRequest, clientKeyInterface, clientStateInterface);
        nextRequest = MappingUtil.map(handleNextRequest);

        // save and print next request

        CLIState.setNextRequest(nextRequest);
        Api.print(nextRequest);

        // done

        return 0;
    }
}
