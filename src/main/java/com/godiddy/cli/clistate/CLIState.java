package com.godiddy.cli.clistate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.api.client.openapi.model.RegistrarState;
import com.godiddy.cli.GodiddyCLIApplication;

import java.util.Map;
import java.util.prefs.Preferences;

public class CLIState {

    private static final Preferences preferences = Preferences.userNodeForPackage(GodiddyCLIApplication.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Object getObject(String key) {
        try {
            String value = preferences.get(key, null);
            String valueClass = preferences.get(key + "Class", null);
            return value == null ? null : objectMapper.readValue(value, Class.forName(valueClass));
        } catch (JsonProcessingException | ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static void setObject(String key, Object value) {
        if (value == null) {
            preferences.remove(key);
            preferences.remove(key + "Class");
        } else {
            try {
                String valueClass = value.getClass().getName();
                preferences.put(key, objectMapper.writeValueAsString(value));
                preferences.put(key + "Class", valueClass);
            } catch (JsonProcessingException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    public static String getMethod() {
        return preferences.get("method", null);
    }

    public static void setMethod(String method) {
        if (method == null) {
            preferences.remove("method");
        } else {
            preferences.put("method", method);
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

    public static Map<String, Object> getClientStateObject() {
        return (Map<String, Object>) getObject("clientStateObject");
    }

    public static void setClientStateObject(Map<String, Object> clientStateObject) {
        setObject("clientStateObject", clientStateObject);
    }
}
