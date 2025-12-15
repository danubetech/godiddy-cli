package com.godiddy.cli.interfaces;

import com.danubetech.kms.clientkeyinterface.ClientKey;
import com.danubetech.kms.clientkeyinterface.ClientKeyInterface;
import com.danubetech.kms.clientkeyinterface.ClientKeyInterfaceFactory;
import com.danubetech.kms.clientkeyinterface.impl.dummy.DummyClientKeyInterface;
import com.danubetech.kms.clientkeyinterface.impl.local.LocalClientKeyInterface;
import com.danubetech.kms.clientkeyinterface.impl.walletservice.WalletServiceClientKeyInterface;
import com.danubetech.kms.clientstateinterface.ClientStateInterface;
import com.danubetech.walletservice.client.WalletServiceClient;
import com.godiddy.cli.clistorage.cliwallet.CLIWallet;
import com.godiddy.cli.config.Kms;
import com.godiddy.cli.config.VaultEndpoint;
import com.godiddy.cli.config.VaultToken;
import com.godiddy.cli.config.WalletServiceBase;
import com.godiddy.cli.interfaces.clientstateinterface.impl.CLIStateClientStateInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Interfaces {

    private static final Logger log = LogManager.getLogger(Interfaces.class);

    public static ClientKeyInterface<? extends ClientKey> instantiateClientKeyInterface() {

        // prepare interfaces

        Kms.Value kms = Kms.getKms();

        ClientKeyInterface<? extends ClientKey> clientKeyInterface = ClientKeyInterfaceFactory.clientKeyInterface(
                kms.toString(),
                CLIWallet::getWallet,
                CLIWallet::setWallet,
                WalletServiceBase.getWalletServiceBase(),
                null,
                VaultEndpoint.getVaultEndpoint(),
                VaultToken.getVaultToken());

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
