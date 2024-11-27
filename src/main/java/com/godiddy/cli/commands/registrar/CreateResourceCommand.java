package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.CreateResourceRequest;
import com.godiddy.api.client.openapi.model.CreateResourceState;
import com.godiddy.api.client.openapi.model.RequestOptions;
import com.godiddy.api.client.openapi.model.RequestSecret;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "createResource",
        description = "Create a DID URL and associated resource, using the Universal Registrar API.",
        mixinStandardHelpOptions = true
)
public class CreateResourceCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(CreateResourceCommand.class);

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
            names = {"-u", "--relativeDidUrl"},
            description = "The requested relative DID URL for the operation."
    )
    String relativeDidUrl;

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
            names = "--content",
            description = "This input field contains the content to be used for the DID create resource operation.",
            defaultValue = "{}"
    )
    String content;

    @Option(
            names = {"-i", "--interactive"},
            description = "This enables interactive mode where the request is prepared but not executed. You can then either run \"godiddy-cli state edit-next\" or \"godiddy-cli state continue-next\"."
    )
    Boolean interactive;

    @Override
    public Integer call() throws Exception {

        // request

        RequestOptions requestOptions = new RequestOptions();
        if (this.options != null) requestOptions.getAdditionalProperties().putAll(this.options);
        if (this.clientSecretMode != null) requestOptions.setClientSecretMode(this.clientSecretMode);
        if (this.network != null) requestOptions.putAdditionalProperty("network", this.network);

        RequestSecret requestSecret = this.secret.isBlank() ? null : Api.fromJson(this.secret, RequestSecret.class);

        String content = this.content.isBlank() ? null : this.content;

        CreateResourceRequest request = new CreateResourceRequest();
        request.setJobId(this.jobId);
        request.setDid(this.did);
        request.setRelativeDidUrl(this.relativeDidUrl);
        request.setOptions(requestOptions);
        request.setSecret(requestSecret);
        request.setContent(content);

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

        CreateResourceState state = Api.execute(() -> Api.universalRegistrarApi().createResourceWithHttpInfo(request));

        // store state

        CLIState.setMethod(null);
        CLIState.setState(null /*state*/);
        CLIState.setPrevRequest(request);
        CLIState.setNextRequest(null);

        // done

        return 0;
    }
}