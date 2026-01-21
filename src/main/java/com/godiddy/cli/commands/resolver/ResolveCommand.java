package com.godiddy.cli.commands.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.openapi.model.ResolutionOptions;
import com.godiddy.api.client.openapi.model.ResolveGetQuery;
import com.godiddy.api.client.openapi.model.ResolvePostBody;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.config.Api;
import foundation.identity.did.representations.Representations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import uniresolver.result.ResolveResult;

import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "resolve",
        description = "Resolve a DID / Dereference a DID URL, using the Universal Resolver API.",
        mixinStandardHelpOptions = true
)
public class ResolveCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ResolveCommand.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Option(
            names = {"-o", "--option"},
            description = "This input field contains a key/value pair contains a DID resolution option / DID URL dereferencing option."
    )
    Map<String, Object> options;

    @Option(
            names = {"-p", "--post"},
            description = "Execute a POST request with a JSON object that contains DID resolution options / DID URL dereferencing options."
    )
    String post;

    @Option(
            names = {"-r", "--result"},
            description = "Whether to request a full DID resolution result including metadata.",
            defaultValue = "false"
    )
    Boolean result;

    @Parameters(
            index = "0",
            description = "The DID to be resolved, or the DID URL to be dereferenced."
    )
    String identifier;

    @Override
    public Integer call() throws Exception {

        // request

        String identifier = this.identifier;
        String accept = Boolean.TRUE.equals(this.result) ? ResolveResult.MEDIA_TYPE : Representations.DEFAULT_MEDIA_TYPE;

        ResolutionOptions resolutionOptions = new ResolutionOptions();
        if (this.post != null && this.post.startsWith("{")) ((Map<String, Object>) objectMapper.readValue(this.post, Map.class)).forEach(resolutionOptions::putAdditionalProperty);
        if (this.options != null) this.options.forEach(resolutionOptions::putAdditionalProperty);

        // execute

        if (this.post == null) {
            ResolveGetQuery resolveGetQuery = new ResolveGetQuery(resolutionOptions);
            Api.execute(() -> Api.universalResolverApi().resolveGetWithHttpInfo(identifier, accept, resolveGetQuery));
        } else {
            ResolvePostBody resolvePostBody = new ResolvePostBody(resolutionOptions);
            Api.execute(() -> Api.universalResolverApi().resolvePostWithHttpInfo(identifier, accept, resolvePostBody));
        }

        // done

        return 0;
    }
}
