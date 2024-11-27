package com.godiddy.cli.commands.localwallet;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import com.godiddy.cli.cliwallet.CLIWallet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "show",
        description = "Show the wallet contents",
        mixinStandardHelpOptions = true
)
public class LocalWalletShowCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(LocalWalletShowCommand.class);

    @CommandLine.Option(
            names = {"-o", "--object"},
            description = "Also print in object notation, in addition to JSON.",
            defaultValue = "false"
    )
    Boolean objectNotation;

    @Override
    public Integer call() throws Exception {
        Object wallet = CLIWallet.getWallet();
        Api.print(wallet);
        if (Boolean.TRUE == this.objectNotation) System.out.println(wallet);
        return 0;
    }
}
