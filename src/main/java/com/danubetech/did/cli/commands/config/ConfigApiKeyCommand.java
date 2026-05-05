package com.danubetech.did.cli.commands.config;

import com.danubetech.did.cli.clistorage.cliconfig.CLIConfig;
import com.danubetech.did.cli.config.ApiKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "apikey",
        description = "Get or set the API key of the API (e.g. Godiddy).",
        mixinStandardHelpOptions = true
)
public class ConfigApiKeyCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigApiKeyCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The API key of the API (e.g. Godiddy).",
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
