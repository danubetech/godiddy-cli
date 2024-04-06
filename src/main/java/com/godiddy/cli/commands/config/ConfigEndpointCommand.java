package com.godiddy.cli.commands.config;

import com.godiddy.cli.api.Endpoint;
import com.godiddy.cli.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "endpoint",
        description = "Get or set the endpoint URL of the Godiddy API. Default value: " + Endpoint.DEFAULT_ENDPOINT + ".",
        mixinStandardHelpOptions = true
)
public class ConfigEndpointCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigEndpointCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The endpoint URL of the Godiddy API. Default value: " + Endpoint.DEFAULT_ENDPOINT + ".",
            arity = "0..1"
    )
    String endpoint;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'endpoint': " + this.endpoint);
        if (Boolean.TRUE.equals(this.remove)) {
            Configuration.setEndpoint(null);
            System.out.println("Endpoint successfully removed.");
        } else {
            if (this.endpoint == null) {
                String endpoint = Configuration.getEndpoint();
                if (endpoint == null) {
                    System.out.println("No endpoint set.");
                } else {
                    System.out.println("Endpoint: " + endpoint);
                }
            } else {
                String endpoint = this.endpoint;
                String predefinedEndpoint = Endpoint.PREDEFINED_ENDPOINTS.get(this.endpoint);
                if (predefinedEndpoint != null) {
                    System.out.println("Using predefined endpoint value '" + predefinedEndpoint + "' for parameter value '" + endpoint + "'.");
                    endpoint = predefinedEndpoint;
                }
                Configuration.setEndpoint(endpoint);
                System.out.println("Endpoint successfully set.");
            }
        }
        return 0;
    }
}
