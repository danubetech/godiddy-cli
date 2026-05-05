package com.danubetech.did.cli;

import com.danubetech.did.cli.config.LogLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;

public class DIDCLIApplication {

    static {
        if (System.getProperty("java.util.logging.manager") == null) {
            System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        }
    }

    private static final Logger log = LogManager.getLogger(DIDCLIApplication.class);

    public static void main(String... args) {
        log.info("Starting application.");
        configureLogLevel();
        AnsiConsole.systemInstall();
        int exitCode = new CommandLine(new DIDRootCommand()).execute(args);
        System.out.println();
        log.info("Exit code: " + exitCode);
        AnsiConsole.systemUninstall();
        System.exit(exitCode);
    }

    private static void configureLogLevel() {
        LogLevel.Value logLevel = LogLevel.getLog();
        switch (logLevel) {
            case warn -> {
                Configurator.setLevel("com.danubetech.did.cli", Level.WARN);
                Configurator.setLevel("com.danubetech", Level.WARN);
            }
            case standard -> {
                Configurator.setLevel("com.danubetech.did.cli", Level.INFO);
                Configurator.setLevel("com.danubetech", Level.WARN);
            }
            case info -> {
                Configurator.setLevel("com.danubetech.did.cli", Level.INFO);
                Configurator.setLevel("com.danubetech", Level.INFO);
            }
            case debug -> {
                Configurator.setLevel("com.danubetech.did.cli", Level.DEBUG);
                Configurator.setLevel("com.danubetech", Level.DEBUG);
            }
            case trace -> {
                Configurator.setLevel("com.danubetech.did.cli", Level.TRACE);
                Configurator.setLevel("com.danubetech", Level.TRACE);
            }
            default -> throw new RuntimeException("Invalid log level: " + logLevel);
        }
    }
}
