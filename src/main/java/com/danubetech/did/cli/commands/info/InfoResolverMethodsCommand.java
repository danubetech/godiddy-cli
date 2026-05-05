package com.danubetech.did.cli.commands.info;

import com.danubetech.did.cli.DIDAbstractCommand;
import com.danubetech.did.cli.config.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "methods",
        description = "Return a list of supported DID methods.",
        mixinStandardHelpOptions = true
)
public class InfoResolverMethodsCommand extends DIDAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(InfoResolverMethodsCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        List<String> result = Api.execute(() -> Api.universalResolverApi().universalResolverGetMethodsWithHttpInfo());

        // done

        return 0;
    }
}
