package com.godiddy.cli.commands.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.clistate.CLIState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.concurrent.Callable;

@Command(
        name = "edit-next",
        description = "Edit the next request.",
        mixinStandardHelpOptions = true
)
public class StateEditNextCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateEditNextCommand.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Integer call() throws Exception {

        // load next request

        RegistrarRequest nextRequest = CLIState.getNextRequest();
        if (nextRequest == null) {
            System.err.println("No next request to edit.");
            return 1;
        }

        // edit next request

        File tempFile = File.createTempFile("next-request-", ".json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, nextRequest);
        ProcessBuilder processBuilder = new ProcessBuilder("/usr/bin/vi", tempFile.getAbsolutePath());
        processBuilder.inheritIO();
        processBuilder.start().waitFor();
        nextRequest = objectMapper.readValue(tempFile, nextRequest.getClass());
        tempFile.deleteOnExit();

        // save and print next reqest

        CLIState.setNextRequest(nextRequest);
        Api.print(nextRequest);

        // done

        return 0;
    }
}
