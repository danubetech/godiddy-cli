package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.swagger.model.CreateRequest;
import com.godiddy.api.client.swagger.model.CreateState;
import com.godiddy.api.client.swagger.model.RegistrarRequestSecret;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.state.State;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

@Command(
        name = "create",
        description = "Create a DID.",
        mixinStandardHelpOptions = true
)
public class CreateCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(CreateCommand.class);

    @Option(
            names = {"-m", "--method"},
            description = "The requested DID method for the operation.",
            required = true
    )
    String method;

    @Option(
            names = {"-j", "--jobId"},
            description = "This input field is used to keep track of an ongoing DID operation process."
    )
    String jobId;

    @Option(
            names = {"-o", "--option"},
            description = "This input field contains a key/value pair with an option for the DID operation, such as the network where the DID operation should be executed."
    )
    Map<String, String> options;

    @Option(
            names = {"-c", "--clientSecretMode"},
            description = "Enables client-managed secret mode. Equivalent to setting -o clientSecretMode=true."
    )
    Boolean clientManagedSecretMode;

    @Option(
            names = {"-n", "--network"},
            description = "The requested network for the operation. Equivalent to setting -o network=<network>."
    )
    String network;

    @Option(
            names = {"-s", "--secret"},
            description = "This input field contains an object with DID controller keys and other secrets needed for performing the DID operation.",
            defaultValue = "{}"
    )
    String secret;

    @Option(
            names = "--diddoc",
            description = "This input field contains the DID document to be used for the DID create operation.",
            defaultValue = "{}"
    )
    String didDocument;

    @Option(
            names = {"-i", "--interactive"},
            description = "This enables interactive mode where the request is prepared but not executed. You can then either run \"godiddy-cli state edit-next\" or \"godiddy-cli state continue-next\"."
    )
    Boolean interactive;

    @Override
    public Integer call() throws Exception {

        // request

        Map<String, Object> options = new LinkedHashMap<>();
        if (this.options != null) options.putAll(this.options);
        if (this.clientManagedSecretMode != null) options.put("clientSecretMode", this.clientManagedSecretMode);
        if (this.network != null) options.put("network", this.network);

        String method = this.method;
        CreateRequest createRequest = new CreateRequest();
        createRequest.setJobId(this.jobId);
        createRequest.setOptions(options);
        createRequest.setSecret(Objects.requireNonNullElse(Api.fromJson(this.secret, RegistrarRequestSecret.class), new RegistrarRequestSecret()));
        createRequest.setDidDocument(Objects.requireNonNullElse(Api.fromJson(this.didDocument), new HashMap<>()));

        // interactive?

        if (Boolean.TRUE.equals(this.interactive)) {
            State.setMethod(null);
            State.setState(null);
            State.setPrev(null);
            State.setNext(createRequest);
            Api.print(createRequest);
            return 0;
        }

        // execute

        CreateState state = Api.execute(() -> Api.universalRegistrarApi().createWithHttpInfo(method, createRequest));

        // handle state

        if ("finished".equalsIgnoreCase(state.getDidState().getState())) {
            State.setMethod(null);
            State.setState(null);
            State.setPrev(null);
            State.setNext(null);
        } else {
            State.setMethod(method);
            State.setState(state);
            State.setPrev(createRequest);
            State.setNext(null);
        }

        // done

        return 0;
    }
}
