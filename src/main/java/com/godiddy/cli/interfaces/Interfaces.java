package com.godiddy.cli.interfaces;

import com.danubetech.kms.clientkeyinterface.ClientKeyInterface;
import com.danubetech.kms.clientkeyinterface.impl.dummy.DummyClientKeyInterface;
import com.danubetech.kms.clientkeyinterface.impl.local.LocalClientKeyInterface;
import com.danubetech.kms.clientkeyinterface.impl.walletservice.WalletServiceClientKeyInterface;
import com.danubetech.kms.clientstateinterface.ClientStateInterface;
import com.danubetech.walletservice.client.WalletServiceClient;
import com.godiddy.cli.api.Kms;
import com.godiddy.cli.api.WalletServiceBase;
import com.godiddy.cli.clistorage.cliwallet.CLIWallet;
import com.godiddy.cli.interfaces.clientstateinterface.impl.CLIStateClientStateInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Interfaces {

    private static final Logger log = LogManager.getLogger(Kms.class);

    public static ClientKeyInterface<?> instantiateClientKeyInterface() {

        // prepare interfaces

        Kms.Value kms = Kms.getKeyInterface();

        ClientKeyInterface<?> clientKeyInterface = switch (kms) {
            case dummy -> new DummyClientKeyInterface();
            case wallet -> new WalletServiceClientKeyInterface(WalletServiceClient.create(WalletServiceBase.getWalletServiceBase()), null);
            case local -> new LocalClientKeyInterface(CLIWallet::getWallet, CLIWallet::setWallet);
            default -> throw new IllegalStateException("Unexpected KMS value: " + kms);
        };

        // done

        log.debug("Instantiated client key interface: {}", clientKeyInterface.getClass().getSimpleName());
        return clientKeyInterface;
    }

    public static ClientStateInterface instantiateClientStateInterface() {

        ClientStateInterface clientStateInterface = new CLIStateClientStateInterface();

        // done

        log.debug("Instantiated client state interface: {}", clientStateInterface.getClass().getSimpleName());
        return clientStateInterface;
    }
}
