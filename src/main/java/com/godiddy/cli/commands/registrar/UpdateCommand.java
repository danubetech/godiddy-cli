package com.godiddy.cli.commands.registrar;

import com.godiddy.api.client.openapi.model.DidDocument;
import com.godiddy.api.client.openapi.model.RequestOptions;
import com.godiddy.api.client.openapi.model.RequestSecret;
import com.godiddy.api.client.openapi.model.UpdateRequest;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.clistorage.clistate.CLIState;
import com.godiddy.cli.config.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.*;
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
            names = {"-rvmi", "--requestVerificationMethodId"},
            description = "An 'id' property for a verification method to generate."
    )
    List<String> requestVerificationMethodId;

    @Option(
            names = {"-rvmt", "--requestVerificationMethodType"},
            description = "A 'type' property for a verification method to generate."
    )
    List<String> requestVerificationMethodType;

    @Option(
            names = {"-rvmp", "--requestVerificationMethodPurpose"},
            description = "A 'type' property for a verification method to generate."
    )
    List<String> requestVerificationMethodPurpose;

    @Option(
            names = {"-rvmj", "--requestVerificationMethodPublicKeyJwk"},
            description = "A 'publicKeyJwk' property for a verification method to generate."
    )
    List<String> requestVerificationMethodPublicKeyJwk;

    @Option(
            names = {"-rvmjk", "--requestVerificationMethodPublicKeyJwkKty"},
            description = "A 'publicKeyJwk.kty' property for a verification method to generate."
    )
    List<String> requestVerificationMethodPublicKeyJwkKty;

    @Option(
            names = {"-rvmjc", "--requestVerificationMethodPublicKeyJwkCrv"},
            description = "A 'publicKeyJwk.crv' property for a verification method to generate."
    )
    List<String> requestVerificationMethodPublicKeyJwkCrv;

    @Option(
            names = {"-s", "--secret"},
            description = "This input field contains an object with DID controller keys and other secrets needed for performing the DID operation."
    )
    Map<String, String> secret;

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
        if (this.options != null) this.options.forEach(requestOptions::putAdditionalProperty);
        if (this.clientSecretMode != null) requestOptions.setClientSecretMode(this.clientSecretMode);
        if (this.requestVerificationMethodId != null) {
            List<Map<String, Object>> requestVerificationMethods = new ArrayList<>();
            for (int i=0; i<this.requestVerificationMethodId.size(); i++) {
                String requestVerificationMethodId = this.requestVerificationMethodId.get(i);
                String requestVerificationMethodType = (this.requestVerificationMethodType == null || this.requestVerificationMethodType.size() < (i+1)) ? null : this.requestVerificationMethodType.get(i);
                String requestVerificationMethodPurpose = (this.requestVerificationMethodPurpose == null || this.requestVerificationMethodPurpose.size() < (i+1)) ? null : this.requestVerificationMethodPurpose.get(i);
                String requestVerificationMethodPublicKeyJwk = (this.requestVerificationMethodPublicKeyJwk == null || this.requestVerificationMethodPublicKeyJwk.size() < (i+1)) ? null : this.requestVerificationMethodPublicKeyJwk.get(i);
                String requestVerificationMethodPublicKeyJwkKty = (this.requestVerificationMethodPublicKeyJwkKty == null || this.requestVerificationMethodPublicKeyJwkKty.size() < (i+1)) ? null : this.requestVerificationMethodPublicKeyJwkKty.get(i);
                String requestVerificationMethodPublicKeyJwkCrv = (this.requestVerificationMethodPublicKeyJwkCrv == null || this.requestVerificationMethodPublicKeyJwkCrv.size() < (i+1)) ? null : this.requestVerificationMethodPublicKeyJwkCrv.get(i);
                Map<String, Object> requestVerificationMethod = new HashMap<>();
                requestVerificationMethod.put("id", requestVerificationMethodId);
                if (requestVerificationMethodType != null) requestVerificationMethod.put("type", requestVerificationMethodType);
                if (requestVerificationMethodPurpose != null) requestVerificationMethod.put("purpose", Api.fromJson(requestVerificationMethodPurpose, List.class));
                if (requestVerificationMethodPublicKeyJwk != null) requestVerificationMethod.put("publicKeyJwk", Api.fromJson(requestVerificationMethodPublicKeyJwk, Map.class));
                if (requestVerificationMethodPublicKeyJwkKty != null) {
                    Map<String, Object> publicKeyJwk = (Map<String, Object>) requestVerificationMethod.computeIfAbsent("publicKeyJwk", x -> new HashMap<String, Object>());
                    publicKeyJwk.put("kty", requestVerificationMethodPublicKeyJwkKty);
                }
                if (requestVerificationMethodPublicKeyJwkCrv != null) {
                    Map<String, Object> publicKeyJwk = (Map<String, Object>) requestVerificationMethod.computeIfAbsent("publicKeyJwk", x -> new HashMap<String, Object>());
                    publicKeyJwk.put("crv", requestVerificationMethodPublicKeyJwkCrv);
                }
                requestVerificationMethods.add(requestVerificationMethod);
            }
            requestOptions.putAdditionalProperty("requestVerificationMethod", requestVerificationMethods);
        }

        RequestSecret requestSecret = new RequestSecret();
        if (this.secret != null) this.secret.forEach(requestSecret::putAdditionalProperty);

        List<String> didDocumentOperation = Collections.singletonList(this.didDocumentOperation);

        List<DidDocument> didDocument = Collections.singletonList(Api.fromJson(this.didDocument, DidDocument.class));

        if (Boolean.TRUE.equals(this.resolve)) {
            Object result = Api.execute(() -> Api.universalResolverApi().resolveWithHttpInfo(this.did, "application/did+ld+json", null));
            didDocument = Collections.singletonList(Api.convert(result, DidDocument.class));
        }

        UpdateRequest request = new UpdateRequest();
        request.setJobId(this.jobId);
        request.setDid(this.did);
        request.setOptions(requestOptions);
        request.setSecret(requestSecret);
        request.setDidDocumentOperation(didDocumentOperation);
        request.setDidDocument(didDocument);

        // store state

        CLIState.setMethod(null);
        CLIState.setState(null);
        CLIState.setPrevRequest(null);
        CLIState.setNextRequest(request);

        // interactive?

        boolean interactive = Boolean.TRUE.equals(this.interactive);

        if (interactive) {
            Api.print(request);
            System.out.println();
            System.out.println("Interactive mode on. Execute 'godiddy-cli continue' to send the next request, or execute 'godiddy-cli state edit-next' to edit it.");
            return 0;
        }

        // continue

        return DoContinue.doContinue(interactive);
    }
}
