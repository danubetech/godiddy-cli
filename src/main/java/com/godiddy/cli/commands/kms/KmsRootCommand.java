package com.godiddy.cli.commands.kms;

import com.godiddy.cli.GodiddyAbstractCommand;
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
public class KmsRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
