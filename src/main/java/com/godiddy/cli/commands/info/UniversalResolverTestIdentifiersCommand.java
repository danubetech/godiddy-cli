package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "testidentifiers",
        description = "Return a map of test identifiers",
        mixinStandardHelpOptions = true
)
public class UniversalResolverTestIdentifiersCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(UniversalResolverTestIdentifiersCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        Object result = Api.universalResolverApi().universalResolverGetTestIdentifiers();

        // response

        System.out.println(Api.toJson(result, true));
        return 0;
    }
}
