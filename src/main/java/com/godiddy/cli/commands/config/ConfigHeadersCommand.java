package com.godiddy.cli.commands.config;

import com.godiddy.cli.api.Headers;
import com.godiddy.cli.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "headers",
        description = "Get or set the display of the Godiddy API headers for display purposes. Default value: " + Headers.DEFAULT_HEADERS + ".",
        mixinStandardHelpOptions = true
)
public class ConfigHeadersCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigHeadersCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The display of the Godiddy API headers. Valid values: ${COMPLETION-CANDIDATES}. Default value: " + Headers.DEFAULT_HEADERS + ".",
            arity = "0..1"
    )
    Headers.Value headers;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'headers': " + this.headers);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setHeaders(null);
            System.out.println("Headers setting successfully deleted.");
        } else {
            if (this.headers == null) {
                Headers.Value headers = CLIConfig.getHeaders();
                if (headers == null) {
                    System.out.println("No headers set.");
                } else {
                    System.out.println("Headers: " + headers);
                }
            } else {
                Headers.Value headers = this.headers;
                Headers.Value predefinedHeaders = Headers.PREDEFINED_HEADERS.get(headers.name());
                if (predefinedHeaders != null) {
                    System.out.println("Using predefined headers value '" + predefinedHeaders + "' for parameter value '" + headers + "'.");
                    headers = predefinedHeaders;
                }
                CLIConfig.setHeaders(headers);
                System.out.println("Headers successfully set.");
            }
        }
        return 0;
    }
}
