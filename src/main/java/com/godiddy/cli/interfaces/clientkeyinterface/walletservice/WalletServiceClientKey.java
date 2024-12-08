package com.godiddy.cli.interfaces.clientkeyinterface.walletservice;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;
import com.danubetech.walletservice.client.openapi.model.Key;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WalletServiceClientKey implements ClientKey {

    private Key key;

    public WalletServiceClientKey() {
    }

    public WalletServiceClientKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WalletServiceClientKey that = (WalletServiceClientKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public String toString() {
        return "WalletServiceClientKey{" +
                "key=" + key +
                '}';
    }

    @Override
    public URI getController() {
        return this.getKey().getController();
    }

    @Override
    public URI getUrl() {
        return this.getKey().getUrl();
    }

    @Override
    public String getType() {
        return this.getKey().getType();
    }

    @Override
    public List<String> getPurpose() {
        return this.getKey().getPurpose();
    }

    @Override
    public Map<String, Object> getPublicKey() {
        Map<String, Object> key = this.getKey().getKey();
        removePrivate(key);
        return key;
    }

    @Override
    public String getVerificationMethodType() {
        Map<String, Object> keyMetadata = this.getKey().getKeyMetadata();
        String verificationMethodType = keyMetadata == null ? null : (String) keyMetadata.get("verificationMethodType");
        return verificationMethodType;
    }

    private static Map<String,Object> removePrivate(Map<String, Object> key) {
        try {
            key.remove("d");
            return key;
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }
}
