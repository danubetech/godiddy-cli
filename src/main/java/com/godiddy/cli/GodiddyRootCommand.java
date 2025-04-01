package com.godiddy.cli;

import com.godiddy.cli.commands.config.ConfigRootCommand;
import com.godiddy.cli.commands.info.InfoRootCommand;
import com.godiddy.cli.commands.kms.KmsRootCommand;
import com.godiddy.cli.commands.registrar.*;
import com.godiddy.cli.commands.resolver.ResolveCommand;
import com.godiddy.cli.commands.resource.ResourceRootCommand;
import com.godiddy.cli.commands.state.StateRootCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "godiddy-cli",
        description = "Godiddy Universal DID Operations",
        footer = "See https://docs.godiddy.com/ for more information.",
        versionProvider = GodiddyRootCommand.GodiddyVersionProvider.class,
        mixinStandardHelpOptions = true,
        subcommands = {
                ConfigRootCommand.class,
                InfoRootCommand.class,
                ResolveCommand.class,
                CreateCommand.class,
                UpdateCommand.class,
                DeactivateCommand.class,
                ExecuteCommand.class,
                ResourceRootCommand.class,
                ContinueCommand.class,
                StateRootCommand.class,
                KmsRootCommand.class
        }
)
public class GodiddyRootCommand extends GodiddyAbstractCommand implements Callable<Integer> {

        static class GodiddyVersionProvider implements CommandLine.IVersionProvider {
                @Override
                public String[] getVersion() {
                        return new String[] { Version.VERSION };
                }
        }

        @Override
        public Integer call() {
                // Your existing implementation
                return 0;
        }
}