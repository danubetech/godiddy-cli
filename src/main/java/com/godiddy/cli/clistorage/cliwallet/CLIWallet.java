package com.godiddy.cli.clistorage.cliwallet;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.cli.clistorage.CLIStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CLIWallet {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static LinkedList<ClientKey> getWallet() {
        try {
            LinkedList<Map> walletMap = CLIStorage.get("wallet") == null ? null : objectMapper.readValue(CLIStorage.get("wallet"), LinkedList.class);
            if (walletMap == null) return null;
            return new LinkedList(walletMap.stream().map(x -> objectMapper.convertValue(x, ClientKey.class)).toList());
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static void setWallet(List<ClientKey> wallet) {
        if (wallet == null) {
            CLIStorage.remove("wallet");
        } else {
            try {
                CLIStorage.put("wallet", objectMapper.writeValueAsString(wallet));
            } catch (JsonProcessingException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }
}
