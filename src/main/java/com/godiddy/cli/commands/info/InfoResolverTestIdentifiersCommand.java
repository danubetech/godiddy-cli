package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "testidentifiers",
        description = "Return a map of test identifiers.",
        mixinStandardHelpOptions = true
)
public class InfoResolverTestIdentifiersCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(InfoResolverTestIdentifiersCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        Object result = Api.execute(() -> Api.universalResolverApi().universalResolverGetTestIdentifiersWithHttpInfo());

        // done

        return 0;
    }
}
