package com.godiddy.cli.commands.wallet;

import com.godiddy.api.client.swagger.model.Key;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.api.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "keys",
        description = "Get key(s) ",
        mixinStandardHelpOptions = true
)
public class WalletGetKeysCommand extends GodiddyAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(WalletGetKeysCommand.class);

    @Option(
            names = {"-c", "--controller"},
            description = "The controller of the key(s) to retrieve.\n"
    )
    String controller;

    @Option(
            names = {"-u", "--url"},
            description = "The URL of the key(s) to retrieve.\n"
    )
    String url;

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

        URI controller = this.controller == null ? null : URI.create(this.controller);
        URI url = this.url == null ? null : URI.create(this.url);
        String type = this.type;
        String purpose = this.purpose;
        Long limit = this.limit;

        // execute

        List<Key> result = Api.execute(() -> Api.walletServiceApi().getKeysWithHttpInfo(controller, url, type, purpose, limit));

        // done

        return 0;
    }
}
