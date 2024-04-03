package com.godiddy.cli.commands.config;

import com.godiddy.cli.GodiddyCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

public abstract class ConfigCommand extends GodiddyCommand implements Callable<Integer> {

        @CommandLine.Option(
                names = {"-d", "--delete"},
                description = "Delete a configuration setting.",
                defaultValue = "false"
        )
        Boolean remove;
}
