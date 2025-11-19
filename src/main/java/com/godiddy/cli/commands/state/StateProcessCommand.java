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
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.config.Api;
import com.godiddy.cli.clistorage.clistate.CLIState;
import com.godiddy.cli.commands.registrar.ContinueCommand;
import com.godiddy.cli.interfaces.Interfaces;
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

        return DoProcess.doProcess(true);
    }
}
