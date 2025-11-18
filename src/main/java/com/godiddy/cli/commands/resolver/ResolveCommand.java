package com.godiddy.cli.commands.resolver;

import com.godiddy.api.client.openapi.model.ResolutionOptions;
import com.godiddy.api.client.openapi.model.ResolveOptionsParameter;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.config.Api;
import foundation.identity.did.representations.Representations;
import foundation.identity.did.representations.consumption.RepresentationConsumerDIDJSON;
import foundation.identity.did.representations.production.RepresentationProducerDID;
import foundation.identity.did.representations.production.RepresentationProducerDIDJSONLD;
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

    @Option(
            names = {"-o", "--option"},
            description = "This input field contains a key/value pair with an option for the DID operation."
    )
    Map<String, Object> options;

    @Option(
            names = {"-r", "--result"},
            description = "Whether or not to request a full DID resolution result including metadata.",
            defaultValue = "false"
    )
    Boolean metadata;

    @Parameters(
            index = "0",
            description = "The DID to be resolved, or the DID URL to be dereferenced."
    )
    String identifier;

    @Override
    public Integer call() throws Exception {

        // request

        String identifier = this.identifier;
        String accept = Boolean.TRUE.equals(this.metadata) ? ResolveResult.MEDIA_TYPE : Representations.DEFAULT_MEDIA_TYPE;

        ResolutionOptions resolutionOptions = this.options == null ? null : new ResolutionOptions();
        if (resolutionOptions != null) this.options.forEach(resolutionOptions::putAdditionalProperty);

        ResolveOptionsParameter resolveOptionsParameter = resolutionOptions == null ? null : new ResolveOptionsParameter(resolutionOptions);

        // execute

        Api.execute(() -> Api.universalResolverApi().resolveWithHttpInfo(identifier, accept, resolveOptionsParameter));

        // done

        return 0;
    }
}
