package com.godiddy.cli.interfaces.clientkeyinterface.local;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;

import java.net.URI;
import java.util.*;

public class LocalClientKey implements ClientKey {

    private UUID id;
    private URI controller;
    private URI url;
    private String type;
    private List<String> purpose;
    private Map<String, Object> privateKey;
    private String verificationMethodType;

    public LocalClientKey() {
    }

    public LocalClientKey(UUID id, URI controller, URI url, String type, List<String> purpose, Map<String, Object> privateKey, String verificationMethodType) {
        this.id = id;
        this.controller = controller;
        this.url = url;
        this.type = type;
        this.purpose = purpose;
        this.privateKey = privateKey;
        this.verificationMethodType = verificationMethodType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public URI getController() {
        return controller;
    }

    public void setController(URI controller) {
        this.controller = controller;
    }

    @Override
    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<String> getPurpose() {
        return purpose;
    }

    public void setPurpose(List<String> purpose) {
        this.purpose = purpose;
    }

    public Map<String, Object> getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(Map<String, Object> privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String getVerificationMethodType() {
        return verificationMethodType;
    }

    public void setVerificationMethodType(String verificationMethodType) {
        this.verificationMethodType = verificationMethodType;
    }

    @Override
    public Map<String, Object> getPublicKey() {
        return removePrivate(new HashMap<>(this.getPrivateKey()));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocalClientKey that = (LocalClientKey) o;
        return Objects.equals(id, that.id) && Objects.equals(controller, that.controller) && Objects.equals(url, that.url) && Objects.equals(type, that.type) && Objects.equals(purpose, that.purpose) && Objects.equals(privateKey, that.privateKey) && Objects.equals(verificationMethodType, that.verificationMethodType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, controller, url, type, purpose, privateKey, verificationMethodType);
    }

    @Override
    public String toString() {
        return "LocalClientKey{" +
                "id=" + id +
                ", controller=" + controller +
                ", url=" + url +
                ", type='" + type + '\'' +
                ", purpose=" + purpose +
                ", privateKey=" + privateKey +
                ", verificationMethodType='" + verificationMethodType + '\'' +
                '}';
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
