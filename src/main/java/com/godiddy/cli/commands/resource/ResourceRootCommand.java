package com.godiddy.cli.commands.resource;

import com.godiddy.cli.GodiddyAbstractCommand;
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
public class ResourceRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {
}
