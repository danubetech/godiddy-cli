package com.godiddy.cli.commands.config;

import com.godiddy.cli.GodiddyCommand;
import com.godiddy.cli.api.Endpoint;
import com.godiddy.cli.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "endpoint",
        description = "Set the endpoint URL of the Godiddy API. Default is \"https://api.godiddy.com/1.0.0/\".",
        mixinStandardHelpOptions = true
)
public class ConfigEndpointCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigEndpointCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "Get or set the endpoint URL of the Godiddy API.",
            arity = "0..1"
    )
    String endpoint;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'endpoint': " + this.endpoint);
        if (this.endpoint == null) {
            String endpoint = Configuration.getEndpoint();
            if (endpoint == null) {
                System.out.println("No endpoint set.");
            } else {
                System.out.println("Endpoint: " + endpoint);
            }
        } else if ("null".equalsIgnoreCase(this.endpoint)) {
            Configuration.setEndpoint(null);
            System.out.println("Endpoint successfully removed.");
        } else {
            String endpoint = this.endpoint;
            String predefinedEndpoint = Endpoint.PREDEFINED_ENDPOINTS.get(endpoint);
            if (predefinedEndpoint != null) {
                System.out.println("Using predefined endpoint value '" + predefinedEndpoint + "' for parameter value '" + endpoint + "'.");
                endpoint = predefinedEndpoint;
            }
            Configuration.setEndpoint(endpoint);
            System.out.println("Endpoint successfully set.");
        }
        return 0;
    }
}
