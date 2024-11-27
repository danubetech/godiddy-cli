package com.godiddy.cli.commands.state.interfaces.walletservice;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;
import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;
import com.danubetech.walletservice.client.WalletServiceClient;
import com.danubetech.walletservice.client.WalletServiceClientException;
import com.danubetech.walletservice.client.openapi.model.Key;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uniregistrar.RegistrationException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WalletServiceClientKeyInterface implements ClientKeyInterface {

    private static final Logger log = LoggerFactory.getLogger(WalletServiceClientKeyInterface.class);

    private static final ModelMapper modelMapper = new ModelMapper();

    private final WalletServiceClient walletServiceClient;
    private final String reference;

    public WalletServiceClientKeyInterface(WalletServiceClient walletServiceClient, String reference) {
        this.walletServiceClient = walletServiceClient;
        this.reference = reference;
    }

    @Override
    public List<ClientKey> getKeys(URI controller, URI url, String type, String purpose) throws RegistrationException {

        List<Key> keys;

        try {
            keys = walletServiceClient.tryRun(w -> w.getKeys(controller, url, type, purpose, WalletServiceClientKeyInterface.this.reference, null));
        } catch (WalletServiceClientException e) {
            throw new RegistrationException("Cannot get keys from wallet service: " + e.getMessage(), e);
        }

        if (log.isDebugEnabled()) log.debug("For controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + " and reference " + reference + " found key(s): " + keys);
        return keys.stream().map(x -> modelMapper.map(x, ClientKey.class)).toList();
    }

    @Override
    public ClientKey getKey(URI controller, URI url, String type, String purpose) throws RegistrationException {

        List<Key> keys;

        try {
            keys = walletServiceClient.tryRun(w -> w.getKeys(controller, url, type, purpose, WalletServiceClientKeyInterface.this.reference, 2L));
        } catch (WalletServiceClientException e) {
            throw new RegistrationException("Cannot get keys from wallet service: " + e.getMessage(), e);
        }

        if (keys.size() > 1) throw new IllegalArgumentException("Unexpected number of keys for controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + " and reference " + reference + ": " + keys.size());
        Key key = keys.isEmpty() ? null : keys.getFirst();

        if (log.isDebugEnabled()) log.debug("For controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + " and reference " + reference + " found key: " + key);
        return key == null ? null : modelMapper.map(key, ClientKey.class);
    }

    @Override
    public ClientKey generateKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) throws RegistrationException {

        Key generateKey = new Key();
        generateKey.setController(controller);
        generateKey.setUrl(url);
        generateKey.setType(type);
        generateKey.setPurpose(purpose);
        generateKey.setKey(key);
        generateKey.setKeyMetadata(keyMetadata);
        generateKey.setReference(reference);

        Key generatedKey;

        try {
            generatedKey = walletServiceClient.tryRun(w -> w.generateKey(generateKey));
        } catch (WalletServiceClientException e) {
            throw new RegistrationException("Cannot generate key in wallet service: " + e.getMessage(), e);
        }

        if (log.isDebugEnabled()) log.debug("For controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + " and key metadata " + keyMetadata + " and reference " + reference + " generated key: " + generatedKey);
        return generatedKey == null ? null : modelMapper.map(generatedKey, ClientKey.class);
    }

    @Override
    public void importKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) throws RegistrationException {

        Key importKey = new Key();

        importKey.setController(controller);
        importKey.setUrl(url);
        importKey.setType(type);
        importKey.setPurpose(purpose);
        importKey.setKey(key);
        importKey.setKeyMetadata(keyMetadata);
        importKey.setReference(reference);

        try {
            walletServiceClient.tryRun(w -> { w.importKey(importKey); return null; });
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For URL " + url + " cannot import key: " + ex.getMessage(), ex);
        }

        if (log.isDebugEnabled()) log.debug("For controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + " and key " + key + " and key metadata " + keyMetadata + " and reference " + reference + " imported key.");
    }

    @Override
    public void updateKey(UUID id, URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) throws RegistrationException {

        Key updateKey = new Key();

        updateKey.setController(controller);
        updateKey.setUrl(url);
        updateKey.setType(type);
        updateKey.setPurpose(purpose);
        updateKey.setKey(key);
        updateKey.setKeyMetadata(keyMetadata);
        updateKey.setReference(reference);

        try {
            walletServiceClient.tryRun(w -> { w.updateKey(id, updateKey); return null; });
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " cannot update key: " + ex.getMessage(), ex);
        }

        if (log.isDebugEnabled()) log.debug("For id " + id + " and controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + " and key " + key + " and key metadata " + keyMetadata + " and reference " + reference + " updated key.");
    }

    @Override
    public void deleteKey(UUID id) throws RegistrationException {

        try {
            walletServiceClient.tryRun(w -> { w.deleteKey(id); return null; });
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " cannot delete key: " + ex.getMessage(), ex);
        }

        if (log.isDebugEnabled()) log.debug("For id " + id + " deleted key.");
    }

    @Override
    public byte[] signWithKey(UUID id, URI url, String algorithm, byte[] content) throws RegistrationException {

        byte[] signature;

        try {
            signature = walletServiceClient.tryRun(w -> w.signWithKey(id, url, algorithm, content));
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " and url " + url + " and algorithm " + algorithm + " cannot sign with key: " + ex.getMessage(), ex);
        }

        if (log.isDebugEnabled()) log.debug("For id " + id + " and url " + url + " and algorithm " + algorithm + " signed with key.");
        return signature;
    }

    @Override
    public byte[] decryptWithKey(UUID id, URI url, String algorithm, byte[] content) throws RegistrationException {

        byte[] decryptedPayload;

        try {
            decryptedPayload = walletServiceClient.tryRun(w -> w.decryptWithKey(id, url, algorithm, content));
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " and url " + url + " and algorithm " + algorithm + " cannot decrypt with key: " + ex.getMessage(), ex);
        }

        if (log.isDebugEnabled()) log.debug("For id " + id + " and url " + url + " and algorithm " + algorithm + " decrypted with key.");
        return decryptedPayload;
    }
}
