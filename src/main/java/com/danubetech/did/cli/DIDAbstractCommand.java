package com.danubetech.did.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

public abstract class DIDAbstractCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        CommandLine.usage(this, System.out);
        return 0;
    }
}
