package com.godiddy.cli.commands.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import com.godiddy.cli.config.LogLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "loglevel",
        description = "Get or set the log level. Default value: " + LogLevel.DEFAULT_LOGLEVEL + ".",
        mixinStandardHelpOptions = true
)
public class ConfigLogLevelCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigLogLevelCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The log level. Valid values: ${COMPLETION-CANDIDATES}. Default value: " + LogLevel.DEFAULT_LOGLEVEL + ".",
            arity = "0..1"
    )
    LogLevel.Value logLevel;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'logLevel': " + this.logLevel);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setLogLevel(null);
            System.out.println("Log level setting successfully deleted.");
        } else {
            if (this.logLevel == null) {
                LogLevel.Value logLevel = CLIConfig.getLogLevel();
                if (logLevel == null) {
                    System.out.println("No log level set.");
                } else {
                    System.out.println("Log level: " + logLevel);
                }
            } else {
                LogLevel.Value logLevel = this.logLevel;
                LogLevel.Value predefinedLogLevel = LogLevel.PREDEFINED_LOGLEVEL.get(logLevel.name());
                if (predefinedLogLevel != null) {
                    System.out.println("Using predefined log level value '" + predefinedLogLevel + "' for parameter value '" + logLevel + "'.");
                    logLevel = predefinedLogLevel;
                }
                CLIConfig.setLogLevel(logLevel);
                System.out.println("Log level successfully set: " + logLevel);
            }
        }
        return 0;
    }
}
