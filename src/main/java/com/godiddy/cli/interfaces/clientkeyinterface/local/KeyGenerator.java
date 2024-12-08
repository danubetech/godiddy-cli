package com.godiddy.cli.interfaces.clientkeyinterface.local;


import bbs.signatures.Bbs;
import com.danubetech.keyformats.PrivateKey_to_JWK;
import com.danubetech.keyformats.crypto.provider.Ed25519Provider;
import com.danubetech.keyformats.crypto.provider.RandomProvider;
import com.danubetech.keyformats.jose.JWK;
import com.danubetech.keyformats.jose.KeyTypeName;
import com.goterl.lazysodium.interfaces.Sign;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import org.bitcoinj.core.ECKey;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.util.Collections;
import java.util.Map;


public class KeyGenerator {

    public static void generateEd25519(LocalClientKey localClientKey) {

        byte[] publicKeyBytesBuffer = new byte[Sign.ED25519_PUBLICKEYBYTES];
        byte[] privateKeyBytesBuffer = new byte[Sign.ED25519_SECRETKEYBYTES];

        try {
            Ed25519Provider.get().generateEC25519KeyPair(publicKeyBytesBuffer, privateKeyBytesBuffer);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        JWK jwk = PrivateKey_to_JWK.Ed25519PrivateKeyBytes_to_JWK(privateKeyBytesBuffer, null, null);
        localClientKey.setType(KeyTypeName.Ed25519.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }

    public static void generateX25519(){
    }

    public static void generateRsa(LocalClientKey localClientKey) {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        JWK jwk = PrivateKey_to_JWK.RSAPrivateKey_to_JWK(keyPair, null, null);
        localClientKey.setType(KeyTypeName.RSA.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }

    public static void generateEcdsaSecp256k1(LocalClientKey localClientKey) {
        ECKey ecKey = new ECKey();
        ECKey newEcKey = ecKey.decompress();

        JWK jwk = PrivateKey_to_JWK.secp256k1PrivateKey_to_JWK(newEcKey,null,null);
        localClientKey.setType(KeyTypeName.secp256k1.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }

    public static void generateBls12381G1(LocalClientKey localClientKey) {
        bbs.signatures.KeyPair keyPair;
        try {
            byte[] seed = RandomProvider.get().randomBytes(32);
            keyPair = Bbs.generateBls12381G1Key(seed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JWK jwk = PrivateKey_to_JWK.Bls12381G1PrivateKey_to_JWK(keyPair, null, null);
        localClientKey.setType(KeyTypeName.Bls12381G1.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }

    public static void generateBls12381G2(LocalClientKey localClientKey) {
        bbs.signatures.KeyPair keyPair;
        try {
            byte[] seed = RandomProvider.get().randomBytes(32);
            keyPair = Bbs.generateBls12381G2Key(seed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JWK jwk = PrivateKey_to_JWK.Bls12381G2PrivateKey_to_JWK(keyPair, null, null);
        localClientKey.setType(KeyTypeName.Bls12381G2.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }

    public static void generateP256(LocalClientKey localClientKey)  {
        ECPrivateKey privateKey = generateECKeys(Curve.P_256);

        JWK jwk = PrivateKey_to_JWK.P_256PrivateKey_to_JWK(privateKey, null, null);
        localClientKey.setType(KeyTypeName.P_256.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }


    public static void generateP384(LocalClientKey localClientKey)  {

        ECPrivateKey privateKey = generateECKeys(Curve.P_384);

        JWK jwk = PrivateKey_to_JWK.P_384PrivateKey_to_JWK(privateKey, null, null);
        localClientKey.setType(KeyTypeName.P_384.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }

    public static void generateP521(LocalClientKey localClientKey)  {

        ECPrivateKey privateKey = generateECKeys(Curve.P_521);

        JWK jwk = PrivateKey_to_JWK.P_521PrivateKey_to_JWK(privateKey, null, null);
        localClientKey.setType(KeyTypeName.P_521.name());
        localClientKey.setPrivateKey(jwk.toMap());
    }

    private static ECPrivateKey generateECKeys(Curve curve ){

        com.nimbusds.jose.jwk.ECKey ecJWK = null;
        try {
            ecJWK = new ECKeyGenerator(curve).generate();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        try {
            return ecJWK.toECPrivateKey();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Helper methods
     */

    private static void removeNullValues(Map<String, Object> key) {
        try {
            key.values().removeAll(Collections.singleton(null));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid key",ex);
        }
    }
}
