package com.godiddy.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class UniversalResolverCommand {

    private static final Logger log = LogManager.getLogger(UniversalResolverCommand.class);

    public static void main(String[] args) {
        log.info("Starting application: " + Arrays.asList(args));
    }
}
