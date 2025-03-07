package com.godiddy.cli.commands.kms;

import com.danubetech.kms.clientkeyinterface.ClientKey;
import com.danubetech.kms.clientkeyinterface.ClientKeyInterface;
import com.godiddy.cli.GodiddyAbstractCommand;
import com.godiddy.cli.config.Api;
import com.godiddy.cli.interfaces.Interfaces;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "controllers",
        description = "Get controller(s).",
        mixinStandardHelpOptions = true
)
public class KmsGetControllersCommand extends GodiddyAbstractCommand implements Callable<Integer> {

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

    @Override
    public Integer call() throws Exception {

        // request

        URI controller = this.controller == null ? null : URI.create(this.controller);
        URI url = this.url == null ? null : URI.create(this.url);
        String type = this.type;
        String purpose = this.purpose;

        // instantiate client key interface

        ClientKeyInterface<?> clientKeyInterface = Interfaces.instantiateClientKeyInterface();

        // execute

        List<? extends ClientKey> clientKeys = clientKeyInterface.getKeys(controller, url, type, purpose);

        // filter controllers

        List<URI> clientKeyControllers = clientKeys.stream().map(ClientKey::getController).toList();

        // done

        Api.print(clientKeyControllers);
        return 0;
    }
}
