package com.godiddy.cli.commands.state.cliinterfaces;

import com.danubetech.uniregistrar.clientstateinterface.ClientStateInterface;

import java.net.URI;

public class CLIClientStateInterface implements ClientStateInterface {

    @Override
    public void putTempKeyController(URI tempKeyController) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public URI getTempKeyController() {
        throw new RuntimeException("Not implemented.");
    }
}
