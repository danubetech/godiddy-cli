package com.godiddy.cli.commands.state.cliinterfaces;

import com.danubetech.uniregistrar.clientstateinterface.ClientStateInterface;
import com.godiddy.cli.clistate.CLIState;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CLIStateClientStateInterface implements ClientStateInterface {

    @Override
    public void putTempKeyController(URI tempKeyController) {
        Map<String, Object> clientStateObject = Objects.requireNonNullElse(CLIState.getClientStateObject(), new LinkedHashMap<>());
        clientStateObject.put("tempKeyController", tempKeyController);
        CLIState.setClientStateObject(clientStateObject);
    }

    @Override
    public URI getTempKeyController() {
        Map<String, Object> clientStateObject = Objects.requireNonNullElse(CLIState.getClientStateObject(), new LinkedHashMap<>());
        String tempKeyController = (String) clientStateObject.get("tempKeyController");
        return tempKeyController == null ? null : URI.create(tempKeyController);
    }
}
