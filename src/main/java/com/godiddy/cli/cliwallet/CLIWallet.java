package com.godiddy.cli.cliwallet;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.cli.GodiddyCLIApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

public class CLIWallet {

    private static final Preferences preferences = Preferences.userNodeForPackage(GodiddyCLIApplication.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static LinkedList<ClientKey> getWallet() {
        try {
            return preferences.get("wallet", null) == null ? null : objectMapper.readValue(preferences.get("wallet", null), LinkedList.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static void setWallet(List<ClientKey> wallet) {
        if (wallet == null) {
            preferences.remove("wallet");
        } else {
            try {
                preferences.put("wallet", objectMapper.writeValueAsString(wallet));
            } catch (JsonProcessingException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }
}
