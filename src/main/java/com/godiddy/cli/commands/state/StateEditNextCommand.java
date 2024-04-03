package com.godiddy.cli.commands.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.state.State;
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
public class StateEditNextCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(StateEditNextCommand.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Integer call() throws Exception {
        Object next = State.getNext();
        if (next == null) {
            System.err.println("No next request to handle.");
            return 1;
        }
        File tempFile = File.createTempFile("next-request-", ".json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, next);
        ProcessBuilder processBuilder = new ProcessBuilder("/usr/bin/vi", tempFile.getAbsolutePath());
        processBuilder.inheritIO();
        processBuilder.start().waitFor();
        next = objectMapper.readValue(tempFile, next.getClass());
        tempFile.deleteOnExit();
        State.setNext(next);
        Api.print(next);
        return 0;
    }
}
