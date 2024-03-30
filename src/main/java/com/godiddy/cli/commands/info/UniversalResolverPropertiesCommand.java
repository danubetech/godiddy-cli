package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "properties",
        description = "Return a map of configuration properties",
        mixinStandardHelpOptions = true
)
public class UniversalResolverPropertiesCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(UniversalResolverPropertiesCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        Object result = Api.universalResolverApi().universalResolverGetProperties();

        // response

        System.out.println(Api.toJson(result, true));
        return 0;
    }
}
