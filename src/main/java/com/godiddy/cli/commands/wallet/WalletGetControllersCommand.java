package com.godiddy.cli.commands.wallet;

import com.godiddy.cli.GodiddyCommand;
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
public class WalletGetControllersCommand extends GodiddyCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(WalletGetControllersCommand.class);

    @Option(
            names = {"-t", "--types"},
            description = "The type of the key(s) to retrieve."
    )
    String type;

    @Option(
            names = {"-p", "--purpose"},
            description = "The purpose(s) of the key(s) to retrieve."
    )
    String purpose;

    @Option(
            names = {"-l", "--limit"},
            description = "The limit (total number) of keys to retrieve."
    )
    Long limit;

    @Option(
            names = {"-p", "--pretty"},
            description = "Pretty-print the result.",
            defaultValue = "true"
    )
    Boolean pretty;

    @Override
    public Integer call() throws Exception {

        // request

        String type = this.type;
        String purpose = this.purpose;
        Long limit = this.limit;

        // execute

        List<String> result = Api.walletServiceApi().getControllers(type, purpose, limit);

        // response

        boolean pretty = Boolean.TRUE.equals(this.pretty);
        System.out.println(Api.toJson(result, pretty));
        return 0;
    }
}
