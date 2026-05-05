package com.danubetech.did.cli.commands.kms;

import com.danubetech.did.cli.DIDAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "kms",
        description = "KMS commands.",
        mixinStandardHelpOptions = true,
        subcommands = {
                KmsGetControllersCommand.class,
                KmsGetKeysCommand.class,
                KmsDeleteCommand.class,
        }
)
public class KmsRootCommand extends DIDAbstractCommand implements Callable<Integer> {
}
