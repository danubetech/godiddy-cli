package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "methods",
        description = "Return a list of supported DID methods",
        mixinStandardHelpOptions = true
)
public class UniversalResolverMethodsCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(UniversalResolverMethodsCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        List<String> result = Api.execute(() -> Api.universalResolverApi().universalResolverGetMethodsWithHttpInfo());

        // done

        return 0;
    }
}