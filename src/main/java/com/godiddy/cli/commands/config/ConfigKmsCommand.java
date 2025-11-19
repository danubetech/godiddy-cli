package com.godiddy.cli.commands.config;

import com.godiddy.cli.config.Kms;
import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "kms",
        description = "Get or set the KMS for client-managed secret mode. Default value: " + Kms.DEFAULT_KMS + ".",
        mixinStandardHelpOptions = true
)
public class ConfigKmsCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigKmsCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The KMS for client-managed secret mode. Valid values: ${COMPLETION-CANDIDATES}. Default value: " + Kms.DEFAULT_KMS + ".",
            arity = "0..1"
    )
    Kms.Value kms;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'kms': " + this.kms);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setKms(null);
            System.out.println("KMS setting successfully deleted.");
        } else {
            if (this.kms == null) {
                Kms.Value kms = CLIConfig.getKms();
                if (kms == null) {
                    System.out.println("No KMS set.");
                } else {
                    System.out.println("KMS: " + kms);
                }
            } else {
                Kms.Value kms = this.kms;
                Kms.Value predefinedKms = Kms.PREDEFINED_KMS.get(kms.name());
                if (predefinedKms != null) {
                    System.out.println("Using predefined KMS value '" + predefinedKms + "' for parameter value '" + kms + "'.");
                    kms = predefinedKms;
                }
                CLIConfig.setKms(kms);
                System.out.println("KMS successfully set: " + kms);
            }
        }
        return 0;
    }
}
