package com.godiddy.cli.commands.config;

import com.godiddy.cli.api.KeyInterface;
import com.godiddy.cli.clistorage.cliconfig.CLIConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "keyinterface",
        description = "Get or set the key interface for client-managed secret mode. Default value: " + KeyInterface.DEFAULT_KEYINTERFACE + ".",
        mixinStandardHelpOptions = true
)
public class ConfigKeyInterfaceCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigKeyInterfaceCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The key interface for client-managed secret mode. Valid values: ${COMPLETION-CANDIDATES}. Default value: " + KeyInterface.DEFAULT_KEYINTERFACE + ".",
            arity = "0..1"
    )
    KeyInterface.Value keyInterface;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'keyInterface': " + this.keyInterface);
        if (Boolean.TRUE.equals(this.delete)) {
            CLIConfig.setKeyInterface(null);
            System.out.println("Key interface setting successfully deleted.");
        } else {
            if (this.keyInterface == null) {
                KeyInterface.Value keyInterface = CLIConfig.getKeyInterface();
                if (keyInterface == null) {
                    System.out.println("No key interface set.");
                } else {
                    System.out.println("Key interface: " + keyInterface);
                }
            } else {
                KeyInterface.Value keyInterface = this.keyInterface;
                KeyInterface.Value predefinedKeyInterface = KeyInterface.PREDEFINED_KEYINTERFACE.get(keyInterface.name());
                if (predefinedKeyInterface != null) {
                    System.out.println("Using predefined key interface value '" + predefinedKeyInterface + "' for parameter value '" + keyInterface + "'.");
                    keyInterface = predefinedKeyInterface;
                }
                CLIConfig.setKeyInterface(keyInterface);
                System.out.println("Key interface successfully set: " + keyInterface);
            }
        }
        return 0;
    }
}
