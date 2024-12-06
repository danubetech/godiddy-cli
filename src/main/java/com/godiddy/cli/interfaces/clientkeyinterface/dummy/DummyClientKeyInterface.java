package com.godiddy.cli.interfaces.clientkeyinterface.dummy;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;
import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DummyClientKeyInterface implements ClientKeyInterface {

    private static final Map<String, Object> DUMMY_KEY = Map.of(
            "kty", "OKP",
            "crv", "Ed25519",
            "x", ".. placeholder ..",
            "y", ".. placeholder .."
    );

    @Override
    public List<ClientKey> getKeys(URI controller, URI url, String type, String purpose) {

        ClientKey clientKey = new ClientKey();
        clientKey.setController(controller);
        clientKey.setUrl(url);
        clientKey.setType(type);
        clientKey.setPurpose(Collections.singletonList(purpose));
        clientKey.setKey(DUMMY_KEY);

        return Collections.singletonList(clientKey);
    }

    @Override
    public ClientKey getKey(URI controller, URI url, String type, String purpose) {

        ClientKey clientKey = new ClientKey();
        clientKey.setController(controller);
        clientKey.setUrl(url);
        clientKey.setType(type);
        clientKey.setPurpose(Collections.singletonList(purpose));
        clientKey.setKey(DUMMY_KEY);

        return clientKey;
    }

    @Override
    public ClientKey generateKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) {

        ClientKey clientKey = new ClientKey();
        clientKey.setController(controller);
        clientKey.setUrl(url);
        clientKey.setType(type);
        clientKey.setPurpose(purpose);
        clientKey.setKey(DUMMY_KEY);

        return clientKey;
    }

    @Override
    public void importKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void updateKey(UUID id, URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void deleteKey(UUID id) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public byte[] signWithKey(UUID id, URI url, String algorithm, byte[] content) {
        return new byte[] { 0, 0, 0 };
    }

    @Override
    public byte[] decryptWithKey(UUID id, URI url, String algorithm, byte[] content) {
        return new byte[] { 0, 0, 0 };
    }
}
