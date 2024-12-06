package com.godiddy.cli.commands.state;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;
import com.danubetech.uniregistrar.clientstateinterface.ClientStateInterface;
import com.danubetech.uniregistrar.local.extensions.handlers.HandleStateUpdateTempKeys;
import com.danubetech.uniregistrar.local.extensions.handlers.HandleStateUpdateVerificationMethods;
import com.danubetech.uniregistrar.local.extensions.handlers.action.HandleActionState;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretJsonWebKeys;
import com.danubetech.uniregistrar.local.extensions.handlers.finished.HandleFinishedStateImportSecretVerificationMethods;
import com.danubetech.walletservice.client.WalletServiceClient;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.api.client.openapi.model.RegistrarState;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.api.KeyInterface;
import com.godiddy.cli.api.WalletServiceBase;
import com.godiddy.cli.clistorage.clistate.CLIState;
import com.godiddy.cli.commands.registrar.ContinueCommand;
import com.godiddy.cli.interfaces.clientkeyinterface.dummy.DummyClientKeyInterface;
import com.godiddy.cli.interfaces.clientkeyinterface.local.LocalClientKeyInterface;
import com.godiddy.cli.interfaces.clientkeyinterface.walletservice.WalletServiceClientKeyInterface;
import com.godiddy.cli.interfaces.clientstateinterface.CLIStateClientStateInterface;
import com.godiddy.cli.util.MappingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "process",
        description = "Process the previous request and current state, and prepare the next request.",
        mixinStandardHelpOptions = true
)
public class StateProcessCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateProcessCommand.class);

    @Override
    public Integer call() throws Exception {

        return doProcess(true);
    }

    public static Integer doProcess(boolean interactive) throws Exception {

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

        KeyInterface.Value keyInterface = KeyInterface.getKeyInterface();

        ClientKeyInterface clientKeyInterface = switch (keyInterface) {
            case dummy -> new DummyClientKeyInterface();
            case wallet -> new WalletServiceClientKeyInterface(WalletServiceClient.create(WalletServiceBase.getWalletServiceBase()), null);
            case local -> new LocalClientKeyInterface();
            default -> throw new IllegalStateException("Unexpected key interface value: " + keyInterface);
        };
        ClientStateInterface clientStateInterface = new CLIStateClientStateInterface();

        // handle

        uniregistrar.openapi.model.RegistrarState handleState = MappingUtil.map(state);
        uniregistrar.openapi.model.RegistrarRequest handlePrevRequest = MappingUtil.map(prevRequest);

        HandleStateUpdateVerificationMethods.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleStateUpdateTempKeys.handleState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleFinishedStateImportSecretJsonWebKeys.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        HandleFinishedStateImportSecretVerificationMethods.handleFinishedState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);

        // handle state

        uniregistrar.openapi.model.RegistrarRequest handleNextRequest = HandleActionState.handleActionState(handlePrevRequest, handleState, clientKeyInterface, clientStateInterface);
        RegistrarRequest nextRequest = MappingUtil.map(handleNextRequest);

        // save next request

        CLIState.setNextRequest(nextRequest);

        // state not handled?

        if (nextRequest == null) {
            return 0;
        }

        // interactive?

        if (interactive) {
            Api.print(nextRequest);
            System.out.println("Interactive mode on. Execute 'godiddy-cli continue' to send the next request, or execute 'godiddy-cli state edit-next' to edit it.");
            return 0;
        }

        // continue

        return ContinueCommand.doContinue(interactive);
    }
}
