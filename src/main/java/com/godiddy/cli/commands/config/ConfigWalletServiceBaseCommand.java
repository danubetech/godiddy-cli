package com.godiddy.cli.commands.config;

import com.godiddy.cli.api.WalletServiceBase;
import com.godiddy.cli.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "walletservicebase",
        description = "Get or set the base URL of the Wallet Service. Default value: " + WalletServiceBase.DEFAULT_WALLETSERVICEBASE + ".",
        mixinStandardHelpOptions = true
)
public class ConfigWalletServiceBaseCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigWalletServiceBaseCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The base URL of the Wallet Service. Default value: " + WalletServiceBase.DEFAULT_WALLETSERVICEBASE + ".",
            arity = "0..1"
    )
    String walletServiceBase;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'walletServiceBase': " + this.walletServiceBase);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setWalletServiceBase(null);
            System.out.println("Wallet service base setting successfully deleted.");
        } else {
            if (this.walletServiceBase == null) {
                String walletServiceBase = CLIConfig.getWalletServiceBase();
                if (walletServiceBase == null) {
                    System.out.println("No wallet service base set.");
                } else {
                    System.out.println("Wallet service base: " + walletServiceBase);
                }
            } else {
                String walletServiceBase = this.walletServiceBase;
                String predefinedWalletServiceBase = WalletServiceBase.PREDEFINED_WALLETSERVICEBASES.get(this.walletServiceBase);
                if (predefinedWalletServiceBase != null) {
                    System.out.println("Using predefined wallet service base value '" + predefinedWalletServiceBase + "' for parameter value '" + walletServiceBase + "'.");
                    walletServiceBase = predefinedWalletServiceBase;
                }
                CLIConfig.setWalletServiceBase(walletServiceBase);
                System.out.println("Wallet service base successfully set.");
            }
        }
        return 0;
    }
}
