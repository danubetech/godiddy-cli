package com.danubetech.did.cli.commands.state;

import com.danubetech.did.api.client.openapi.model.RegistrarRequest;
import com.danubetech.did.cli.DIDAbstractCommand;
import com.danubetech.did.cli.clistorage.clistate.CLIState;
import com.danubetech.did.cli.config.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "prev",
        description = "Return the previous request in the current state.",
        mixinStandardHelpOptions = true
)
public class StatePrevCommand extends DIDAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StatePrevCommand.class);

    @CommandLine.Option(
            names = {"-o", "--object"},
            description = "Also print in object notation, in addition to JSON.",
            defaultValue = "false"
    )
    Boolean objectNotation;

    @Override
    public Integer call() throws Exception {
        RegistrarRequest prevRequest = CLIState.getPrevRequest();
        Api.print(prevRequest);
        if (Boolean.TRUE == this.objectNotation) System.out.println(prevRequest);
        return 0;
    }
}
