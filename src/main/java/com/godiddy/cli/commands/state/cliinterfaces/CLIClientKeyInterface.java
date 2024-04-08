package com.godiddy.cli.commands.state.cliinterfaces;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;
import com.danubetech.uniregistrar.clientkeyinterface.Key;
import uniregistrar.RegistrationException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CLIClientKeyInterface implements ClientKeyInterface {

    @Override
    public List<Key> getKeys(URI controller, URI url, String type, String purpose) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Key getKey(URI controller, URI url, String type, String purpose) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Key generateKey(URI controller, URI url, String type, String purpose, Map<String, Object> key, Map<String, Object> keyMetadata) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void importKey(URI controller, URI url, String type, String purpose, Map<String, Object> key, Map<String, Object> keyMetadata) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void updateKey(UUID id, URI controller, URI url, String type, String purpose, Map<String, Object> key, Map<String, Object> keyMetadata) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void deleteKey(UUID id) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public byte[] signWithKey(UUID id, URI url, String algorithm, byte[] content) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public byte[] decryptWithKey(UUID id, URI url, String algorithm, byte[] content) throws RegistrationException {
        throw new RuntimeException("Not implemented.");
    }
}
