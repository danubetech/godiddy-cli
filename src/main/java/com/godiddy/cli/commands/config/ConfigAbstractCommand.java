package com.godiddy.cli.commands.config;

import com.godiddy.cli.GodiddyAbstractCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

public abstract class ConfigAbstractCommand extends GodiddyAbstractCommand implements Callable<Integer> {

        @CommandLine.Option(
                names = {"-d", "--delete"},
                description = "Delete a configuration setting.",
                defaultValue = "false"
        )
        Boolean delete;
}
