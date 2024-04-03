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
public class UniversalRegistrarPropertiesCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(UniversalRegistrarPropertiesCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        Object result = Api.execute(() -> Api.universalRegistrarApi().universalRegistrarGetMethodsWithHttpInfo());

        // done

        return 0;
    }
}
