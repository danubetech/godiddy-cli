package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.*;
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
        name = "update",
        description = "Update a DID, using the Universal Registrar API.",
        mixinStandardHelpOptions = true
)
public class UpdateCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(UpdateCommand.class);

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
            names = "--diddocop",
            description = "This input field contains the DID document operation to be used for the DID update operation.",
            defaultValue = "setDidDocument"
    )
    String didDocumentOperation;

    @Option(
            names = "--diddoc",
            description = "This input field contains the DID document to be used for the DID update operation.",
            defaultValue = "{}"
    )
    String didDocument;

    @Option(
            names = {"-r", "--resolve"},
            description = "This resolves the current DID document as a basis for the DID update operation."
    )
    Boolean resolve;

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

        List<String> didDocumentOperation = Collections.singletonList(this.didDocumentOperation);

        List<DidDocument> didDocument = Collections.singletonList(Api.fromJson(this.didDocument, DidDocument.class));

        if (Boolean.TRUE.equals(this.resolve)) {
            Object result = Api.execute(() -> Api.universalResolverApi().resolveWithHttpInfo(this.did, "application/did+ld+json"));
            didDocument = Collections.singletonList(Api.convert(result, DidDocument.class));
        }

        UpdateRequest request = new UpdateRequest();
        request.setJobId(this.jobId);
        request.setDid(this.did);
        request.setOptions(requestOptions);
        request.setSecret(requestSecret);
        request.setDidDocumentOperation(didDocumentOperation);
        request.setDidDocument(didDocument);

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

        UpdateState state = Api.execute(() -> Api.universalRegistrarApi().updateWithHttpInfo(request));

        // store state

        CLIState.setMethod(null);
        CLIState.setState(state);
        CLIState.setPrevRequest(request);
        CLIState.setNextRequest(null);

        // done

        return 0;
    }
}
