package com.godiddy.cli.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.cli.GodiddyCLIApplication;

import java.util.prefs.Preferences;

public class State {

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
                preferences.put(key, objectMapper.writeValueAsString(value));
                preferences.put(key + "Class", value.getClass().getName());
            } catch (JsonProcessingException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    private static String getMethod() {
        return preferences.get("method", null);
    }

    public static void setMethod(String method) {
        if (method == null) {
            preferences.remove("method");
        } else {
            preferences.put("method", method);
        }
    }

    public static Object getState() {
        return getObject("state");
    }

    public static void setState(Object state) {
        setObject("state", state);
    }

    public static Object getPrev() {
        return getObject("prev");
    }

    public static void setPrev(Object prev) {
        setObject("prev", prev);
    }

    public static Object getNext() {
        return getObject("next");
    }

    public static void setNext(Object next) {
        setObject("next", next);
    }
}
