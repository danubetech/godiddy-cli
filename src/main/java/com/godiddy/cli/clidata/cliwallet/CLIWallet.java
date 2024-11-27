package com.godiddy.cli.clidata.cliwallet;

import com.danubetech.uniregistrar.clientkeyinterface.ClientKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.cli.clidata.CLIData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CLIWallet {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static LinkedList<ClientKey> getWallet() {
        try {
            LinkedList<Map> walletMap = CLIData.get("wallet") == null ? null : objectMapper.readValue(CLIData.get("wallet"), LinkedList.class);
            if (walletMap == null) return null;
            return new LinkedList(walletMap.stream().map(x -> objectMapper.convertValue(x, ClientKey.class)).toList());
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static void setWallet(List<ClientKey> wallet) {
        if (wallet == null) {
            CLIData.remove("wallet");
        } else {
            try {
                CLIData.put("wallet", objectMapper.writeValueAsString(wallet));
            } catch (JsonProcessingException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }
}
