package com.godiddy.cli.interfaces.clientkeyinterface.local;

import com.danubetech.keyformats.JWK_to_PrivateKey;
import com.danubetech.keyformats.crypto.PrivateKeySigner;
import com.danubetech.keyformats.crypto.PrivateKeySignerFactory;
import com.danubetech.keyformats.jose.JWK;
import com.danubetech.keyformats.jose.KeyTypeName;
import com.danubetech.keyformats.keytypes.KeyTypeName_for_JWK;
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

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        List<LocalClientKey> localClientKeys = wallet != null ? wallet : Collections.emptyList();

        if (controller != null) localClientKeys = localClientKeys.stream().filter(clientKey -> controller.equals(clientKey.getController())).toList();
        if (url != null) localClientKeys = localClientKeys.stream().filter(clientKey -> url.equals(clientKey.getUrl())).toList();
        if (type != null) localClientKeys = localClientKeys.stream().filter(clientKey -> type.equals(clientKey.getType())).toList();
        if (purpose != null) localClientKeys = localClientKeys.stream().filter(clientKey -> clientKey.getPurpose() != null && clientKey.getPurpose().contains(purpose)).toList();

        return localClientKeys;
    }

    @Override
    public LocalClientKey getKey(URI controller, URI url, String type, String purpose) {

        List<LocalClientKey> localClientKeys = this.getKeys(controller, url, type, purpose);
        LocalClientKey localClientKey = localClientKeys.isEmpty() ? null : localClientKeys.getFirst();

        return localClientKey;
    }

    private LocalClientKey loadKey(UUID id) {

        if (id == null) throw new IllegalArgumentException("'id' is missing.");

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) return null;

        LocalClientKey localClientKey = wallet.stream().filter(k -> id.equals(k.getId())).findFirst().orElse(null);
        return localClientKey;
    }

    @Override
    public LocalClientKey generateKey(URI controller, URI url, String type, List<String> purpose, String verificationMethodType) {

        LocalClientKey localClientKey = new LocalClientKey();

        localClientKey.setController(controller);
        localClientKey.setUrl(url);
        localClientKey.setType(type);
        localClientKey.setPurpose(purpose);
        localClientKey.setVerificationMethodType(verificationMethodType);

        // generate key based on type

        if (KeyTypeName.RSA.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateRsa(localClientKey);
        } else if (KeyTypeName.Bls12381G1.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateBls12381G1(localClientKey);
        } else if (KeyTypeName.Bls12381G2.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateBls12381G2(localClientKey);
        } else if (KeyTypeName.secp256k1.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateEcdsaSecp256k1(localClientKey);
        } else if (KeyTypeName.Ed25519.equals(KeyTypeName.from(type))) {
            KeyGenerator.generateEd25519(localClientKey);
        } else if(KeyTypeName.P_256.equals(KeyTypeName.from(type))){
            KeyGenerator.generateP256(localClientKey);
        } else if(KeyTypeName.P_384.equals(KeyTypeName.from(type))){
            KeyGenerator.generateP384(localClientKey);
        } else if(KeyTypeName.P_521.equals(KeyTypeName.from(type))){
            KeyGenerator.generateP521(localClientKey);
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + type);
        }

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) wallet = new LinkedList<>();
        wallet.add(localClientKey);
        CLIWallet.setWallet(wallet);

        if (log.isDebugEnabled()) log.debug("Generated key with controller " + controller + " and url " + url + " and type " + type + " and purpose " + purpose + " and verification method type " + verificationMethodType);
        return localClientKey;
    }

    @Override
    public void importKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, String verificationMethodType) {

        if (key == null) throw new IllegalArgumentException("Key materials are missing.");

        LocalClientKey localClientKey = new LocalClientKey();

        localClientKey.setId(UUID.randomUUID());
        localClientKey.setController(controller);
        localClientKey.setUrl(url);
        localClientKey.setType(type);
        localClientKey.setPurpose(purpose);
        localClientKey.setPrivateKey(key);
        localClientKey.setVerificationMethodType(verificationMethodType);

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) wallet = new LinkedList<>();

        wallet.add(localClientKey);
        CLIWallet.setWallet(wallet);
    }

    @Override
    public void updateKey(LocalClientKey clientKey, URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, String verificationMethodType) {

        if (clientKey == null) throw new NullPointerException();

        LinkedList<LocalClientKey> wallet = CLIWallet.getWallet();
        if (wallet == null) throw new IllegalArgumentException("No key found for 'id' " + clientKey.getId());

        LocalClientKey updateClientKey = wallet.stream().filter(c -> clientKey.getId().equals(c.getId())).findFirst().orElse(null);
        if (updateClientKey == null) throw new IllegalArgumentException("No key found for 'id' " + clientKey.getId());

        if (controller != null) updateClientKey.setController(controller);
        if (url != null) updateClientKey.setUrl(url);
        if (type != null) updateClientKey.setType(type);
        if (purpose != null) updateClientKey.setPurpose(purpose);
        if (key != null) updateClientKey.setPrivateKey(key);
        if (verificationMethodType != null) updateClientKey.setVerificationMethodType(verificationMethodType);

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
    public byte[] signWithKey(LocalClientKey localClientKey, String algorithm, byte[] content) {

        if (localClientKey == null) throw new NullPointerException();

        // obtain signer

        PrivateKeySigner<?> privateKeySigner;

        try {

            JWK jsonWebKey = JWK.fromMap(localClientKey.getPrivateKey());

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

        if (log.isDebugEnabled()) log.debug("Signed " + content.length + " bytes with key controller " + localClientKey.getController() + " and key url " + localClientKey.getUrl() + " and algorithm " + algorithm);
        return signature;
    }

    @Override
    public byte[] decryptWithKey(LocalClientKey clientKey, String algorithm, byte[] content) {
        throw new RuntimeException("Not implemented.");
    }
}
