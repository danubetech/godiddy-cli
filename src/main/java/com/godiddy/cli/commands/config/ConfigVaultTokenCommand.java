package com.godiddy.cli.commands.config;

import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import com.godiddy.cli.config.VaultToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "vaulttoken",
        description = "Get or set the token of a Hashicorp Vault instance. Default value: " + VaultToken.DEFAULT_VAULTTOKEN + ".",
        mixinStandardHelpOptions = true
)
public class ConfigVaultTokenCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigVaultTokenCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The token of a Hashicorp Vault instance. Default value: " + VaultToken.DEFAULT_VAULTTOKEN + ".",
            arity = "0..1"
    )
    String vaultToken;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'vaultToken': " + this.vaultToken);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setVaultToken(null);
            System.out.println("Vault token setting successfully deleted.");
        } else {
            if (this.vaultToken == null) {
                String vaultToken = CLIConfig.getVaultToken();
                if (vaultToken == null) {
                    System.out.println("No vault token set.");
                } else {
                    System.out.println("Vault token: " + vaultToken);
                }
            } else {
                String vaultToken = this.vaultToken;
                String predefinedVaultToken = VaultToken.PREDEFINED_VAULTTOKENS.get(this.vaultToken);
                if (predefinedVaultToken != null) {
                    System.out.println("Using predefined vault token value '" + predefinedVaultToken + "' for parameter value '" + vaultToken + "'.");
                    vaultToken = predefinedVaultToken;
                }
                CLIConfig.setVaultToken(vaultToken);
                System.out.println("Vault token successfully set: " + vaultToken);
            }
        }
        return 0;
    }
}
