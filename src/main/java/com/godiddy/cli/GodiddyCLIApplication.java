package com.godiddy.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;

import java.util.Arrays;

public class GodiddyCLIApplication {

    private static final Logger log = LogManager.getLogger(GodiddyCLIApplication.class);

    public static void main(String... args) {
        log.info("Starting application: " + Arrays.asList(args));
        AnsiConsole.systemInstall();
        int exitCode = new CommandLine(new GodiddyRootCommand()).execute(args);
        System.out.println();
        log.info("Exit code: " + exitCode);
        AnsiConsole.systemUninstall();
        System.exit(exitCode);
    }
}
