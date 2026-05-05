package com.danubetech.did.cli;

import com.danubetech.did.cli.commands.config.ConfigRootCommand;
import com.danubetech.did.cli.commands.info.InfoRootCommand;
import com.danubetech.did.cli.commands.kms.KmsRootCommand;
import com.danubetech.did.cli.commands.registrar.*;
import com.danubetech.did.cli.commands.resolver.ResolveCommand;
import com.danubetech.did.cli.commands.resource.ResourceRootCommand;
import com.danubetech.did.cli.commands.state.StateRootCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "did-cli",
        description = "CLI for Universal DID Operations",
        footer = "See https://docs.godiddy.com/ for more information.",
        versionProvider = DIDRootCommand.DIDCLIVersionProvider.class,
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
public class DIDRootCommand extends DIDAbstractCommand implements Callable<Integer> {

        static class DIDCLIVersionProvider implements CommandLine.IVersionProvider {
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