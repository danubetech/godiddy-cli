package com.danubetech.did.cli.commands.resource;

import com.danubetech.did.cli.DIDAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "resource",
        description = "DID URL resource commands.",
        mixinStandardHelpOptions = true,
        subcommands = {
                CreateResourceCommand.class,
                UpdateResourceCommand.class,
                DeactivateResourceCommand.class
        }
)
public class ResourceRootCommand extends DIDAbstractCommand implements Callable<Integer> {
}
