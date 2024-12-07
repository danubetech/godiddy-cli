package com.godiddy.cli.interfaces.clientkeyinterface.local;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LocalClientKey extends ClientKey {

    private UUID id;

    public LocalClientKey() {
    }

    public LocalClientKey(URI controller, URI url, String type, List<String> purpose, Map<String, Object> key, Map<String, Object> keyMetadata, UUID id) {
        super(controller, url, type, purpose, key, keyMetadata);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LocalClientKey that = (LocalClientKey) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "LocalClientKey{" +
                "id=" + id +
                "} " + super.toString();
    }
}
