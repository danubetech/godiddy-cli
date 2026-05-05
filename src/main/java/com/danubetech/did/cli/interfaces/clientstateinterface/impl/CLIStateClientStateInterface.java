package com.danubetech.did.cli.interfaces.clientstateinterface.impl;

import com.danubetech.did.cli.clistorage.clistate.CLIState;
import com.danubetech.kms.clientstateinterface.ClientStateInterface;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CLIStateClientStateInterface implements ClientStateInterface {

    @Override
    public void putTempController(URI tempController) {
        Map<String, Object> clientStateObject = Objects.requireNonNullElse(CLIState.getClientStateObject(), new LinkedHashMap<>());
        clientStateObject.put("tempController", tempController);
        CLIState.setClientStateObject(clientStateObject);
    }

    @Override
    public URI getTempController() {
        Map<String, Object> clientStateObject = Objects.requireNonNullElse(CLIState.getClientStateObject(), new LinkedHashMap<>());
        String tempController = (String) clientStateObject.get("tempController");
        return tempController == null ? null : URI.create(tempController);
    }
}
