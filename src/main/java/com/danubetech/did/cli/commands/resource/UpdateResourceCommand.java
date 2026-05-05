package com.danubetech.did.cli.commands.resource;

import com.danubetech.did.api.client.openapi.model.RegistrarRequestJobId;
import com.danubetech.did.api.client.openapi.model.RequestOptions;
import com.danubetech.did.api.client.openapi.model.RequestSecret;
import com.danubetech.did.api.client.openapi.model.UpdateResourceRequest;
import com.danubetech.did.cli.DIDAbstractCommand;
import com.danubetech.did.cli.clistorage.clistate.CLIState;
import com.danubetech.did.cli.commands.registrar.DoContinue;
import com.danubetech.did.cli.config.Api;
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
        description = "Update a DID URL and associated resource, using the Universal Registrar API.",
        mixinStandardHelpOptions = true
)
public class UpdateResourceCommand extends DIDAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(UpdateResourceCommand.class);

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
            names = {"-s", "--secret"},
            description = "This input field contains an object with DID controller keys and other secrets needed for performing the DID operation."
    )
    Map<String, String> secret;

    @Option(
            names = "--contentop",
            description = "This input field contains the content operation to be used for the DID update resource operation.",
            defaultValue = "setContent"
    )
    String contentOperation;

    @Option(
            names = "--content",
            description = "This input field contains the Base64-encoded content to be used for the DID update resource operation.",
            defaultValue = "{}"
    )
    String content;

    @Option(
            names = {"-i", "--interactive"},
            description = "This enables interactive mode where the request is prepared but not executed. You can then either run \"did-cli state edit-next\" or \"did-cli continue\"."
    )
    Boolean interactive;

    @Override
    public Integer call() throws Exception {

        // request

        RequestOptions requestOptions = new RequestOptions();
        if (this.options != null) this.options.forEach(requestOptions::putAdditionalProperty);
        if (this.clientSecretMode != null) requestOptions.setClientSecretMode(this.clientSecretMode);

        RequestSecret requestSecret = new RequestSecret();
        if (this.secret != null) this.secret.forEach(requestSecret::putAdditionalProperty);

        RegistrarRequestJobId registrarRequestJobId = this.jobId == null ? null : new RegistrarRequestJobId(this.jobId);

        List<String> contentOperation = this.contentOperation.isBlank() ? Collections.emptyList() : Collections.singletonList(this.contentOperation);

        List<String> content = this.content.isBlank() ? Collections.emptyList() : Collections.singletonList(this.content);

        UpdateResourceRequest request = new UpdateResourceRequest();
        request.setJobId(registrarRequestJobId);
        request.setDid(this.did);
        request.setRelativeDidUrl(this.relativeDidUrl);
        request.setOptions(requestOptions);
        request.setSecret(requestSecret);
        request.setContentOperation(contentOperation);
        request.setContent(content);

        // store state

        CLIState.setMethod(null);
        CLIState.setState(null);
        CLIState.setPrevRequest(null);
        CLIState.setNextRequest(request);
        CLIState.setClientStateObject(null);

        // interactive?

        boolean interactive = Boolean.TRUE.equals(this.interactive);

        if (interactive) {
            Api.print(request);
            System.out.println();
            System.out.println("Interactive mode on. Execute 'did-cli continue' to send the next request, or execute 'did-cli state edit-next' to edit it.");
            return 0;
        }

        // continue

        return DoContinue.doContinue(interactive);
    }
}
