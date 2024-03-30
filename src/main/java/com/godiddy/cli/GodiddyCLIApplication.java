package com.godiddy.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.Arrays;

public class GodiddyCLIApplication {

    private static final Logger log = LogManager.getLogger(GodiddyCLIApplication.class);

    public static void main(String... args) {
        log.info("Starting application: " + Arrays.asList(args));
        int exitCode = new CommandLine(new GodiddyRootCommand()).execute(args);
        log.info("Exit code: " + exitCode);
        System.exit(exitCode);
    }
}
