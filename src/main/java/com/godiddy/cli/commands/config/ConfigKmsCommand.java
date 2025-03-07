package com.godiddy.cli.commands.config;

import com.godiddy.cli.config.Kms;
import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "kms",
        description = "Get or set the key interface for client-managed secret mode. Default value: " + Kms.DEFAULT_KMS + ".",
        mixinStandardHelpOptions = true
)
public class ConfigKmsCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigKmsCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The key interface for client-managed secret mode. Valid values: ${COMPLETION-CANDIDATES}. Default value: " + Kms.DEFAULT_KMS + ".",
            arity = "0..1"
    )
    Kms.Value keyInterface;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'keyInterface': " + this.keyInterface);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setKms(null);
            System.out.println("Key interface setting successfully deleted.");
        } else {
            if (this.keyInterface == null) {
                Kms.Value keyInterface = CLIConfig.getKms();
                if (keyInterface == null) {
                    System.out.println("No key interface set.");
                } else {
                    System.out.println("Key interface: " + keyInterface);
                }
            } else {
                Kms.Value keyInterface = this.keyInterface;
                Kms.Value predefinedKeyInterface = Kms.PREDEFINED_KMS.get(keyInterface.name());
                if (predefinedKeyInterface != null) {
                    System.out.println("Using predefined key interface value '" + predefinedKeyInterface + "' for parameter value '" + keyInterface + "'.");
                    keyInterface = predefinedKeyInterface;
                }
                CLIConfig.setKms(keyInterface);
                System.out.println("Key interface successfully set: " + keyInterface);
            }
        }
        return 0;
    }
}
