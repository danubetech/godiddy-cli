package com.godiddy.cli.commands.resolver;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import foundation.identity.did.representations.consumption.RepresentationConsumerDIDJSON;
import foundation.identity.did.representations.production.RepresentationProducerDID;
import foundation.identity.did.representations.production.RepresentationProducerDIDJSONLD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
        name = "resolve",
        description = "Resolve a DID / Dereference a DID URL, using the Universal Resolver API.",
        mixinStandardHelpOptions = true
)
public class ResolveCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ResolveCommand.class);

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
        String accept = Boolean.TRUE.equals(this.metadata) ? "application/ld+json;profile=\"https://w3id.org/did-resolution\"": "" + RepresentationProducerDID.MEDIA_TYPE + "," + RepresentationProducerDIDJSONLD.MEDIA_TYPE + "," + RepresentationConsumerDIDJSON.MEDIA_TYPE;

        // execute

        Api.execute(() -> Api.universalResolverApi().resolveWithHttpInfo(identifier, accept));

        // done

        return 0;
    }
}
