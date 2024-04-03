package com.godiddy.cli.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.cli.GodiddyCLIApplication;

import java.util.prefs.Preferences;

public class State {

    private static final Preferences preferences = Preferences.userNodeForPackage(GodiddyCLIApplication.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Object get(String key) {
        try {
            String value = preferences.get(key, null);
            String valueClass = preferences.get(key + "Class", null);
            return value == null ? null : objectMapper.readValue(value, Class.forName(valueClass));
        } catch (JsonProcessingException | ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static void set(String key, Object value) {
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

    public static Object getState() {
        return get("state");
    }

    public static void setState(Object state) {
        set("state", state);
    }

    public static Object getPrev() {
        return get("prev");
    }

    public static void setPrev(Object prev) {
        set("prev", prev);
    }

    public static Object getNext() {
        return get("next");
    }

    public static void setNext(Object next) {
        set("next", next);
    }
}
