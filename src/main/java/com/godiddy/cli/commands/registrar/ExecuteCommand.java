package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.ExecuteRequest;
import com.godiddy.api.client.openapi.model.ExecuteState;
import com.godiddy.api.client.openapi.model.RequestOptions;
import com.godiddy.api.client.openapi.model.RequestSecret;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "execute",
        description = "Execute a DID operation, using the Universal Registrar API.",
        mixinStandardHelpOptions = true
)
public class ExecuteCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ExecuteCommand.class);

    @Option(
            names = {"-j", "--jobId"},
            description = "This input field is used to keep track of an ongoing DID operation process."
    )
    String jobId;

    @Option(
            names = {"-d", "--did"},
            description = "The requested DID for the operation.",
            required = true
    )
    String did;

    @Option(
            names = {"-o", "--option"},
            description = "This input field contains a key/value pair with an option for the DID operation, such as the network where the DID operation should be executed."
    )
    Map<String, String> options;

    @Option(
            names = {"-c", "--clientSecretMode"},
            description = "Enables client-managed secret mode. Equivalent to setting -o clientSecretMode=true."
    )
    Boolean clientSecretMode;

    @Option(
            names = {"-s", "--secret"},
            description = "This input field contains an object with DID controller keys and other secrets needed for performing the DID operation.",
            defaultValue = "{}"
    )
    String secret;

    @Option(
            names = "--op",
            description = "This input field contains the operation to be used for the DID execute operation.",
            required = true
    )
    String operation;

    @Option(
            names = "--opdata",
            description = "This input field contains the data to be used for the DID execute operation.",
            defaultValue = "{}"
    )
    Map<String, Object> operationData;

    @Option(
            names = {"-i", "--interactive"},
            description = "This enables interactive mode where the request is prepared but not executed. You can then either run \"godiddy-cli state edit-next\" or \"godiddy-cli continue\"."
    )
    Boolean interactive;

    @Override
    public Integer call() throws Exception {

        // request

        RequestOptions requestOptions = new RequestOptions();
        if (this.options != null) requestOptions.getAdditionalProperties().putAll(this.options);
        if (this.clientSecretMode != null) requestOptions.setClientSecretMode(this.clientSecretMode);

        RequestSecret requestSecret = this.secret.isBlank() ? null : Api.fromJson(this.secret, RequestSecret.class);

        List<String> operation = Collections.singletonList(this.operation);

        List<Object> operationData = Collections.singletonList(this.operationData);

        ExecuteRequest request = new ExecuteRequest();
        request.setJobId(this.jobId);
        request.setDid(this.did);
        request.setOptions(requestOptions);
        request.setSecret(requestSecret);
        request.setOperation(operation);
        request.setOperationData(operationData);

        // interactive?

        if (Boolean.TRUE.equals(this.interactive)) {
            CLIState.setMethod(null);
            CLIState.setState(null);
            CLIState.setPrevRequest(null);
            CLIState.setNextRequest(request);
            Api.print(request);
            return 0;
        }

        // execute

        ExecuteState state = Api.execute(() -> Api.universalRegistrarApi().executeWithHttpInfo(request));

        // store state

        CLIState.setMethod(null);
        CLIState.setState(state);
        CLIState.setPrevRequest(request);
        CLIState.setNextRequest(null);

        // done

        return 0;
    }
}