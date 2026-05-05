package com.danubetech.did.cli.commands.info;

import com.danubetech.did.cli.DIDAbstractCommand;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "registrar",
        description = "Information about the Universal Registrar API.",
        mixinStandardHelpOptions = true,
        subcommands = {
                InfoRegistrarPropertiesCommand.class,
                InfoRegistrarMethodsCommand.class
        }
)
public class InfoRegistrarCommand extends DIDAbstractCommand implements Callable<Integer> {
}
