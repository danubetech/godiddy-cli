package com.godiddy.cli.interfaces.clientkeyinterface.local;

import com.danubetech.keyformats.JWK_to_PrivateKey;
import com.danubetech.keyformats.crypto.PrivateKeySigner;
import com.danubetech.keyformats.crypto.PrivateKeySignerFactory;
import com.danubetech.keyformats.jose.JWK;
import com.danubetech.keyformats.jose.KeyTypeName;
import com.danubetech.keyformats.keytypes.KeyTypeName_for_JWK;
import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;
import com.danubetech.uniregistrar.clientkeyinterface.ClientKeyInterface;
import com.godiddy.cli.clistorage.cliwallet.CLIWallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.*;

public class LocalClientKeyInterface implements ClientKeyInterface<LocalClientKey> {

    private static final Logger log = LoggerFactory.getLogger(LocalClientKeyInterface.class);

    @Override
    public List<LocalClientKey> getKeys(URI controller, URI url, String type, String purpose) {

        List<LocalClientKey> keys = getKeysPrivate(controller, url, type, purpose);

        if (controller != null) keys = keys.stream().filter(clientKey -> controller.equals(clientKey.getController())).toList();
        if (url != null) keys = keys.stream().filter(clientKey -> url.equals(clientKey.getUrl())).toList();
        if (type != null) keys = keys.stream().filter(clientKey -> type.equals(clientKey.getType())).toList();
        if (purpose != null) keys = keys.stream().filter(clientKey -> clientKey.getPurpose() != null && clientKey.getPurpose().contains(purpose)).toList();

        keys.parallelStream().forEach(k -> k.setKey(removePrivate(k.getKey())));

        return keys;
    }

    private static List<LocalClientKey> getKeysPrivate(URI controller, URI url, String type, String purpose) {

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        List<LocalClientKey> keys = wallet != null ? wallet : Collections.emptyList();

        if (controller != null) keys = keys.stream().filter(clientKey -> controller.equals(clientKey.getController())).toList();
        if (url != null) keys = keys.stream().filter(clientKey -> url.equals(clientKey.getUrl())).toList();
        if (type != null) keys = keys.stream().filter(clientKey -> type.equals(clientKey.getType())).toList();
        if (purpose != null) keys = keys.stream().filter(clientKey -> clientKey.getPurpose() != null && clientKey.getPurpose().contains(purpose)).toList();

        return keys;
    }

    @Override
    public LocalClientKey getKey(URI controller, URI url, String type, String purpose) {

        List<LocalClientKey> keys = this.getKeys(controller, url, type, purpose);

        LocalClientKey key = keys.isEmpty() ? null : keys.getFirst();
        if (key != null) key.setKey(removePrivate(key.getKey()));

        return key;
    }

    private LocalClientKey getKeyPrivate(URI controller, URI url, String type, String purpose) {

        List<LocalClientKey> keys = this.getKeysPrivate(controller, url, type, purpose);

        ClientKey key = keys.isEmpty() ? null : keys.getFirst();
        return key;
    }

    public LocalClientKey getKey(UUID id) {

        if (id == null) throw new IllegalArgumentException("'id' is missing.");

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) return null;

        ClientKey clientKey = wallet.stream().filter(clientKey1 -> id.equals(clientKey1.getId())).findFirst().orElse(null);
        if (clientKey != null) clientKey.setKey(removePrivate(clientKey.getKey()));
        return clientKey;
    }

    private ClientKey getKeyPrivate(UUID id) {

        if (id == null) throw new IllegalArgumentException("'id' is missing.");

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) return null;

        ClientKey clientKey = wallet.stream().filter(clientKey1 -> id.equals(clientKey1.getId())).findFirst().orElse(null);
        return clientKey;
    }

    @Override
    public LocalClientKey generateKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) {

        LocalClientKey clientKey = generateKeyPrivate(controller, url, type, purpose, key, keyMetadata);

        clientKey.setKey(removePrivate(clientKey.getKey()));

        if (log.isDebugEnabled()) log.debug("Generated key with id controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose);
        return clientKey;
    }

    @Override
    public void importKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) {

        if (key == null) throw new IllegalArgumentException("Key materials are missing.");

        LocalClientKey clientKey = new LocalClientKey();

        clientKey.setId(UUID.randomUUID());
        clientKey.setController(controller);
        clientKey.setUrl(url);
        clientKey.setType(type);
        clientKey.setPurpose(purpose);
        clientKey.setKey(key);
        clientKey.setKeyMetadata(keyMetadata);

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) wallet = new LinkedList<>();

        wallet.add(clientKey);
        CLIWallet.setWallet(wallet);
    }

    @Override
    public void updateKey(LocalClientKey clientKey, URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) {

        if (clientKey == null) throw new NullPointerException();

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) throw new IllegalArgumentException("No key found for 'id' " + clientKey.getId());

        LocalClientKey updateClientKey = wallet.stream().filter(c -> clientKey.getId().equals(c.getId())).findFirst().orElse(null);
        if (updateClientKey == null) throw new IllegalArgumentException("No key found for 'id' " + clientKey.getId());

        if (controller != null) updateClientKey.setController(controller);
        if (url != null) updateClientKey.setUrl(url);
        if (type != null) updateClientKey.setType(type);
        if (purpose != null) updateClientKey.setPurpose(purpose);
        if (key != null) updateClientKey.setKey(key);
        if (keyMetadata != null) updateClientKey.setKeyMetadata(keyMetadata);

        CLIWallet.setWallet(wallet);
    }

    @Override
    public void deleteKey(LocalClientKey clientKey) {

        if (clientKey == null) throw new NullPointerException();

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) throw new IllegalArgumentException("No key found for 'id' " + clientKey.getId());

        boolean removed = wallet.removeIf(c -> clientKey.getId().equals(c.getId()));
        CLIWallet.setWallet(wallet);

        if (! removed) throw new IllegalArgumentException("No key found for 'id' " + clientKey.getId());
    }

    @Override
    public byte[] signWithKey(LocalClientKey clientKey, String algorithm, byte[] content) {

        if (clientKey == null) throw new NullPointerException();

        clientKey = this.getKeyPrivate(clientKey.getId());
        if (clientKey == null) throw new IllegalArgumentException("No key found for 'id' " + clientKey.getId());

        if (log.isDebugEnabled()) log.debug("Found key with id {}: ", clientKey.getKey());

        // obtain signer

        PrivateKeySigner<?> privateKeySigner;

        try {

            JWK jsonWebKey = JWK.fromMap(key.getKey());

            KeyTypeName keyTypeName = KeyTypeName_for_JWK.keyTypeName_for_JWK(jsonWebKey);
            Object privateKey = JWK_to_PrivateKey.JWK_to_anyPrivateKey(jsonWebKey);

            privateKeySigner = PrivateKeySignerFactory.privateKeySignerForKey(keyTypeName, algorithm, privateKey);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot obtain signer: " + ex.getMessage(), ex);
        }

        // sign

        byte[] signature;

        if (log.isDebugEnabled()) log.debug("Signing {} bytes with algorithm {}", content.length, algorithm);

        try {

            signature = privateKeySigner.sign(content, algorithm);
            if (log.isDebugEnabled()) log.debug("Signature is {} bytes.", signature.length);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException("Cannot sign: " + ex.getMessage(), ex);
        }

        // done

        if (log.isDebugEnabled()) log.debug("Signed " + content.length + " bytes with key id " + id + " and controller " + key.getController() + " and url " + url + " and algorithm " + algorithm);
        return signature;
    }

    @Override
    public byte[] decryptWithKey(LocalClientKey clientKey, String algorithm, byte[] content) {
        throw new RuntimeException("Not implemented.");
    }

    /*
     * Private key methods
     */

    private static LocalClientKey generateKeyPrivate(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata) {

        LocalClientKey clientKey = new LocalClientKey();

        clientKey.setId(UUID.randomUUID());
        clientKey.setController(controller);
        clientKey.setUrl(url);
        clientKey.setType(type);
        clientKey.setPurpose(purpose);

        // generate key based on type

        if (KeyTypeName.RSA.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateRsa(clientKey);
            removeNullValues(clientKey.getKey());
        } else if (KeyTypeName.Bls12381G1.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateBls12381G1(clientKey);
            removeNullValues(clientKey.getKey());
        } else if (KeyTypeName.Bls12381G2.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateBls12381G2(clientKey);
            removeNullValues(clientKey.getKey());
        } else if (KeyTypeName.secp256k1.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateEcdsaSecp256k1(clientKey);
            removeNullValues(clientKey.getKey());
        } else if (KeyTypeName.Ed25519.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateEd25519(clientKey);
            removeNullValues(clientKey.getKey());
        } else if(KeyTypeName.P_256.equals(KeyTypeName.from(type))){
            KeyGenerator.generateP256(clientKey);
            removeNullValues(clientKey.getKey());
        } else if(KeyTypeName.P_384.equals(KeyTypeName.from(type))){
            KeyGenerator.generateP384(clientKey);
            removeNullValues(clientKey.getKey());
        } else if(KeyTypeName.P_521.equals(KeyTypeName.from(type))){
            KeyGenerator.generateP521(clientKey);
            removeNullValues(clientKey.getKey());
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + type);
        }

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) wallet = new LinkedList<>();
        wallet.add(clientKey);
        CLIWallet.setWallet(wallet);

        return clientKey;
    }

    /*
     * Helper methods
     */

    private static Map<String,Object> removePrivate(Map<String, Object> key) {
        try {
            key.remove("d");
            return key;
        } catch (Exception ex) {
            log.warn("no private component 'd' in private key",ex);
            return Collections.emptyMap();
        }
    }

    private static void removeNullValues(Map<String, Object> key) {
        try {
            key.values().removeAll(Collections.singleton(null));
        } catch (Exception ex) {
            log.error("Invalid key ",ex);
            throw new IllegalArgumentException("Invalid key",ex);
        }
    }
}
