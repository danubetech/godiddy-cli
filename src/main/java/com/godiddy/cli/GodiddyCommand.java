package com.godiddy.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

public abstract class GodiddyCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        CommandLine.usage(this, System.out);
        return 0;
    }
}
