package com.godiddy.cli.commands.info;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.config.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "properties",
        description = "Return a map of configuration properties.",
        mixinStandardHelpOptions = true
)
public class InfoRegistrarPropertiesCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(InfoRegistrarPropertiesCommand.class);

    @Override
    public Integer call() throws Exception {

        // request

        // execute

        Object result = Api.execute(() -> Api.universalRegistrarApi().universalRegistrarGetMethodsWithHttpInfo());

        // done

        return 0;
    }
}
