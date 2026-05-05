package com.danubetech.did.cli.commands.config;

import com.danubetech.did.cli.DIDAbstractCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

public abstract class ConfigAbstractCommand extends DIDAbstractCommand implements Callable<Integer> {

        @CommandLine.Option(
                names = {"-d", "--delete"},
                description = "Delete a configuration setting.",
                defaultValue = "false"
        )
        Boolean delete;
}
