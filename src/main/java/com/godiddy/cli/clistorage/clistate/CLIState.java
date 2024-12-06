package com.godiddy.cli.clistorage.clistate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.api.client.openapi.model.RegistrarState;
import com.godiddy.cli.clistorage.CLIStorage;

import java.util.Map;

public class CLIState {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Object getObject(String key) {
        try {
            String value = CLIStorage.get(key);
            String valueClass = CLIStorage.get(key + "Class");
            return value == null ? null : objectMapper.readValue(value, Class.forName(valueClass));
        } catch (JsonProcessingException | ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static void setObject(String key, Object value) {
        if (value == null) {
            CLIStorage.remove(key);
            CLIStorage.remove(key + "Class");
        } else {
            try {
                String valueClass = value.getClass().getName();
                CLIStorage.put(key, objectMapper.writeValueAsString(value));
                CLIStorage.put(key + "Class", valueClass);
            } catch (JsonProcessingException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    public static String getMethod() {
        return CLIStorage.get("method");
    }

    public static void setMethod(String method) {
        if (method == null) {
            CLIStorage.remove("method");
        } else {
            CLIStorage.put("method", method);
        }
    }

    public static RegistrarState getState() {
        return (RegistrarState) getObject("state");
    }

    public static void setState(RegistrarState state) {
        setObject("state", state);
    }

    public static RegistrarRequest getPrevRequest() {
        return (RegistrarRequest) getObject("prevRequest");
    }

    public static void setPrevRequest(RegistrarRequest prevRequest) {
        setObject("prevRequest", prevRequest);
    }

    public static RegistrarRequest getNextRequest() {
        return (RegistrarRequest) getObject("nextRequest");
    }

    public static void setNextRequest(RegistrarRequest nextRequest) {
        setObject("nextRequest", nextRequest);
    }

    public static Map<String, Object> getClientKeyObject() {
        return (Map<String, Object>) getObject("clientKeyObject");
    }

    public static void setClientKeyObject(Map<String, Object> clientKeyObject) {
        setObject("clientKeyObject", clientKeyObject);
    }

    public static Map<String, Object> getClientStateObject() {
        return (Map<String, Object>) getObject("clientStateObject");
    }

    public static void setClientStateObject(Map<String, Object> clientStateObject) {
        setObject("clientStateObject", clientStateObject);
    }
}
