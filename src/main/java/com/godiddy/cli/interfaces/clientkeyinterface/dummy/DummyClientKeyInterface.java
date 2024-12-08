package com.godiddy.cli.interfaces.clientkeyinterface.dummy;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DummyClientKeyInterface implements ClientKeyInterface<DummyClientKey> {

    private static final Map<String, Object> DUMMY_KEY = Map.of(
            "kty", "OKP",
            "crv", "Ed25519",
            "x", ".. placeholder ..",
            "y", ".. placeholder .."
    );

    @Override
    public List<DummyClientKey> getKeys(URI controller, URI url, String type, String purpose) {

        DummyClientKey clientKey = new DummyClientKey();
        clientKey.setController(controller);
        clientKey.setUrl(url);
        clientKey.setType(type);
        clientKey.setPurpose(Collections.singletonList(purpose));
        clientKey.setPrivateKey(DUMMY_KEY);

        return Collections.singletonList(clientKey);
    }

    @Override
    public DummyClientKey getKey(URI controller, URI url, String type, String purpose) {

        DummyClientKey dummyClientKey = new DummyClientKey();
        dummyClientKey.setController(controller);
        dummyClientKey.setUrl(url);
        dummyClientKey.setType(type);
        dummyClientKey.setPurpose(Collections.singletonList(purpose));
        dummyClientKey.setPrivateKey(DUMMY_KEY);

        return dummyClientKey;
    }

    @Override
    public DummyClientKey generateKey(URI controller, URI url, String type, List<String> purpose, String verificationMethodType) {

        DummyClientKey dummyClientKey = new DummyClientKey();
        dummyClientKey.setController(controller);
        dummyClientKey.setUrl(url);
        dummyClientKey.setType(type);
        dummyClientKey.setPurpose(purpose);
        dummyClientKey.setPrivateKey(DUMMY_KEY);

        return dummyClientKey;
    }

    @Override
    public void importKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, String verificationMethodType) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void updateKey(DummyClientKey dummyClientKey, URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, String verificationMethodType) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void deleteKey(DummyClientKey dummyClientKey) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public byte[] signWithKey(DummyClientKey dummyClientKey, String algorithm, byte[] content) {
        return new byte[] { 0, 0, 0 };
    }

    @Override
    public byte[] decryptWithKey(DummyClientKey dummyClientKey, String algorithm, byte[] content) {
        return new byte[] { 0, 0, 0 };
    }
}
