package com.danubetech.did.cli.commands.kms;

import com.danubetech.did.cli.DIDAbstractCommand;
import com.danubetech.did.cli.interfaces.Interfaces;
import com.danubetech.kms.clientkeyinterface.ClientKey;
import com.danubetech.kms.clientkeyinterface.ClientKeyInterface;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "delete",
        description = "Delete key(s).",
        mixinStandardHelpOptions = true
)
public class KmsDeleteCommand extends DIDAbstractCommand implements Callable<Integer> {

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

        final ClientKeyInterface<ClientKey> clientKeyInterface = (ClientKeyInterface<ClientKey>) Interfaces.instantiateClientKeyInterface();

        // execute

        List<ClientKey> clientKeys = clientKeyInterface.getKeys(controller, url, type, purpose);
        for (ClientKey clientKey : clientKeys) clientKeyInterface.deleteKey(clientKey);

        // done

        return 0;
    }
}
