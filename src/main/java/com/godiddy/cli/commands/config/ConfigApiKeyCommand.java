package com.godiddy.cli.commands.config;

import com.godiddy.cli.api.ApiKey;
import com.godiddy.cli.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "apikey",
        description = "Get or set the API key of the Godiddy API.",
        mixinStandardHelpOptions = true
)
public class ConfigApiKeyCommand extends ConfigCommand implements Callable<Integer> {

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
        if (Boolean.TRUE.equals(this.remove)) {
            Configuration.setApiKey(null);
            System.out.println("API key successfully removed.");
        } else {
            if (this.apiKey == null) {
                String apiKey = Configuration.getApiKey();
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
                Configuration.setApiKey(apiKey);
                System.out.println("API key successfully set.");
            }
        }
        return 0;
    }
}
