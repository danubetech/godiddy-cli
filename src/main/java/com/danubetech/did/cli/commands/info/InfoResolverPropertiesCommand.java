package com.danubetech.did.cli.commands.info;

import com.danubetech.did.cli.DIDAbstractCommand;
import com.danubetech.did.cli.config.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "properties",
        description = "Return a map of configuration properties.",
        mixinStandardHelpOptions = true
)
public class InfoResolverPropertiesCommand extends DIDAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(InfoResolverPropertiesCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        Object result = Api.execute(() -> Api.universalResolverApi().universalResolverGetPropertiesWithHttpInfo());

        // done

        return 0;
    }
}
