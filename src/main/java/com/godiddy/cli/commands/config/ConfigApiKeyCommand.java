package com.godiddy.cli.commands.config;

import com.godiddy.cli.api.ApiKey;
import com.godiddy.cli.clidata.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "apikey",
        description = "Get or set the API key of the Godiddy API.",
        mixinStandardHelpOptions = true
)
public class ConfigApiKeyCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigApiKeyCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The API key of the Godiddy API.",
            arity = "0..1"
    )
    String apiKey;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'apiKey': " + this.apiKey);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setApiKey(null);
            System.out.println("API key setting successfully deleted.");
        } else {
            if (this.apiKey == null) {
                String apiKey = CLIConfig.getApiKey();
                if (apiKey == null) {
                    System.out.println("No API key set.");
                } else {
                    System.out.println("API key: " + apiKey);
                }
            } else {
                String apiKey = this.apiKey;
                String predefinedApiKey = ApiKey.PREDEFINED_APIKEYS.get(this.apiKey);
                if (predefinedApiKey != null) {
                    System.out.println("Using predefined API key value '" + predefinedApiKey + "' for parameter value '" + apiKey + "'.");
                    apiKey = predefinedApiKey;
                }
                CLIConfig.setApiKey(apiKey);
                System.out.println("API key successfully set: " + apiKey);
            }
        }
        return 0;
    }
}
