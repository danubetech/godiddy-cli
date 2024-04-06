package com.godiddy.cli.commands.wallet;

import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "controllers",
        description = "Get controller(s) ",
        mixinStandardHelpOptions = true
)
public class WalletGetControllersCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(WalletGetControllersCommand.class);

    @Option(
            names = {"-t", "--type"},
            description = "The type of the key(s) to retrieve."
    )
    String type;

    @Option(
            names = {"-o", "--purpose"},
            description = "The purpose(s) of the key(s) to retrieve."
    )
    String purpose;

    @Option(
            names = {"-l", "--limit"},
            description = "The limit (total number) of keys to retrieve.",
            defaultValue = "10"
    )
    Long limit;

    @Override
    public Integer call() throws Exception {

        // request

        String type = this.type;
        String purpose = this.purpose;
        Long limit = this.limit;

        // execute

        List<String> result = Api.execute(() -> Api.walletServiceApi().getControllersWithHttpInfo(type, purpose, limit));

        // done

        return 0;
    }
}
