package com.godiddy.cli.interfaces.clientkeyinterface.walletservice;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;
import com.danubetech.walletservice.client.WalletServiceClient;
import com.danubetech.walletservice.client.WalletServiceClientException;
import com.danubetech.walletservice.client.openapi.model.Key;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uniregistrar.RegistrationException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WalletServiceClientKeyInterface implements ClientKeyInterface<WalletServiceClientKey> {

    private static final Logger log = LoggerFactory.getLogger(WalletServiceClientKeyInterface.class);

    private static final ModelMapper modelMapper = new ModelMapper();

    private final WalletServiceClient walletServiceClient;
    private final String reference;

    public WalletServiceClientKeyInterface(WalletServiceClient walletServiceClient, String reference) {
        this.walletServiceClient = walletServiceClient;
        this.reference = reference;
    }

    @Override
    public List<WalletServiceClientKey> getKeys(URI controller, URI url, String type, String purpose) throws RegistrationException {

        List<Key> keys;

        try {
            keys = this.getWalletServiceClient().tryRun(w -> w.getKeys(controller, url, type, purpose, WalletServiceClientKeyInterface.this.getReference(), null));
        } catch (WalletServiceClientException e) {
            throw new RegistrationException("Cannot get keys from wallet service: " + e.getMessage(), e);
        }

        if (log.isInfoEnabled()) log.info("Found " + keys.size() + " key(s) with controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose);
        return keys.stream().map(WalletServiceClientKey::new).toList();
    }

    @Override
    public WalletServiceClientKey getKey(URI controller, URI url, String type, String purpose) throws RegistrationException {

        List<Key> keys;

        try {
            keys = this.getWalletServiceClient().tryRun(w -> w.getKeys(controller, url, type, purpose, WalletServiceClientKeyInterface.this.getReference(), 2L));
        } catch (WalletServiceClientException e) {
            throw new RegistrationException("Cannot get keys from wallet service: " + e.getMessage(), e);
        }

        if (keys.size() > 1) throw new IllegalArgumentException("Unexpected number of keys for controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + ": " + keys.size());
        Key key = keys.isEmpty() ? null : keys.getFirst();

        if (log.isInfoEnabled()) log.info("Found key with controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose);
        return key == null ? null : new WalletServiceClientKey(key);
    }

    @Override
    public WalletServiceClientKey generateKey(URI controller, URI url, String type, List<String> purpose, String verificationMethodType) throws RegistrationException {

        Map<String, Object> keyMetadata = verificationMethodType == null ? new HashMap<>() : Map.of("verificationMethodType", verificationMethodType);

        Key generateKey = new Key();
        generateKey.setController(controller);
        generateKey.setUrl(url);
        generateKey.setType(type);
        generateKey.setPurpose(purpose);
        generateKey.setKeyMetadata(keyMetadata);
        generateKey.setReference(this.getReference());

        Key generatedKey;

        try {
            generatedKey = this.getWalletServiceClient().tryRun(w -> w.generateKey(generateKey));
        } catch (WalletServiceClientException e) {
            throw new RegistrationException("Cannot generate key in wallet service: " + e.getMessage(), e);
        }

        if (log.isInfoEnabled()) log.info("Generated key with controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose);
        return generatedKey == null ? null : new WalletServiceClientKey(generatedKey);
    }

    @Override
    public void importKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, String verificationMethodType) throws RegistrationException {

        Key importKey = new Key();

        Map<String, Object> keyMetadata = verificationMethodType == null ? new HashMap<>() : Map.of("verificationMethodType", verificationMethodType);

        importKey.setController(controller);
        importKey.setUrl(url);
        importKey.setType(type);
        importKey.setPurpose(purpose);
        importKey.setKey(key);
        importKey.setKeyMetadata(keyMetadata);
        importKey.setReference(this.getReference());

        try {
            this.getWalletServiceClient().tryRun(w -> { w.importKey(importKey); return null; });
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For URL " + url + " cannot import key: " + ex.getMessage(), ex);
        }

        if (log.isInfoEnabled()) log.info("Imported key with controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose);
    }

    @Override
    public void updateKey(WalletServiceClientKey walletServiceClientKey, URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, String verificationMethodType) throws RegistrationException {

        UUID id = walletServiceClientKey.getKey().getId();

        Key updateKey = new Key();

        Map<String, Object> keyMetadata = verificationMethodType == null ? new HashMap<>() : Map.of("verificationMethodType", verificationMethodType);

        updateKey.setController(controller);
        updateKey.setUrl(url);
        updateKey.setType(type);
        updateKey.setPurpose(purpose);
        updateKey.setKey(key);
        updateKey.setKeyMetadata(keyMetadata);
        updateKey.setReference(this.getReference());

        try {
            this.getWalletServiceClient().tryRun(w -> { w.updateKey(id, updateKey); return null; });
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " cannot update key: " + ex.getMessage(), ex);
        }

        if (log.isInfoEnabled()) log.info("Updated key with id " + id + " and controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose);
    }

    @Override
    public void deleteKey(WalletServiceClientKey walletServiceClientKey) throws RegistrationException {

        UUID id = walletServiceClientKey.getKey().getId();

        try {
            this.getWalletServiceClient().tryRun(w -> { w.deleteKey(id); return null; });
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " cannot delete key: " + ex.getMessage(), ex);
        }

        if (log.isInfoEnabled()) log.info("Deleted key with id " + id);
    }

    @Override
    public byte[] signWithKey(WalletServiceClientKey walletServiceClientKey, String algorithm, byte[] content) throws RegistrationException {

        UUID id = walletServiceClientKey.getKey().getId();
        URI url = walletServiceClientKey.getKey().getUrl();

        byte[] signature;

        try {
            signature = this.getWalletServiceClient().tryRun(w -> w.signWithKey(id, url, algorithm, content));
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " and url " + url + " and algorithm " + algorithm + " cannot sign with key: " + ex.getMessage(), ex);
        }

        if (log.isInfoEnabled()) log.info("Signed " + content.length + " bytes with key id " + id + " and url " + url + " and algorithm " + algorithm);
        return signature;
    }

    @Override
    public byte[] decryptWithKey(WalletServiceClientKey walletServiceClientKey, String algorithm, byte[] content) throws RegistrationException {

        UUID id = walletServiceClientKey.getKey().getId();
        URI url = walletServiceClientKey.getKey().getUrl();

        byte[] decryptedPayload;

        try {
            decryptedPayload = this.getWalletServiceClient().tryRun(w -> w.decryptWithKey(id, url, algorithm, content));
        } catch (WalletServiceClientException ex) {
            throw new RegistrationException("For id " + id + " and url " + url + " and algorithm " + algorithm + " cannot decrypt with key: " + ex.getMessage(), ex);
        }

        if (log.isInfoEnabled()) log.info("Decrypted " + content.length + " bytes with key id " + id + " and url " + url + " and algorithm " + algorithm);
        return decryptedPayload;
    }

    public WalletServiceClient getWalletServiceClient() {
        return walletServiceClient;
    }

    public String getReference() {
        return reference;
    }
}
