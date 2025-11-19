package com.godiddy.cli.commands.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import com.godiddy.cli.config.VaultEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "vaultendpoint",
        description = "Get or set the endpoint of a Hashicorp Vault instance. Default value: " + VaultEndpoint.DEFAULT_VAULTENDPOINT + ".",
        mixinStandardHelpOptions = true
)
public class ConfigVaultEndpointCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigVaultEndpointCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The endpoint of a Hashicorp Vault instance. Default value: " + VaultEndpoint.DEFAULT_VAULTENDPOINT + ".",
            arity = "0..1"
    )
    String vaultEndpoint;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'vaultEndpoint': " + this.vaultEndpoint);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setVaultEndpoint(null);
            System.out.println("Vault endpoint setting successfully deleted.");
        } else {
            if (this.vaultEndpoint == null) {
                String vaultEndpoint = CLIConfig.getVaultEndpoint();
                if (vaultEndpoint == null) {
                    System.out.println("No vault endpoint set.");
                } else {
                    System.out.println("Vault endpoint: " + vaultEndpoint);
                }
            } else {
                String vaultEndpoint = this.vaultEndpoint;
                String predefinedVaultEndpoint = VaultEndpoint.PREDEFINED_VAULTENDPOINTS.get(this.vaultEndpoint);
                if (predefinedVaultEndpoint != null) {
                    System.out.println("Using predefined vault endpoint value '" + predefinedVaultEndpoint + "' for parameter value '" + vaultEndpoint + "'.");
                    vaultEndpoint = predefinedVaultEndpoint;
                }
                CLIConfig.setVaultEndpoint(vaultEndpoint);
                System.out.println("Vault endpoint successfully set: " + vaultEndpoint);
            }
        }
        return 0;
    }
}
