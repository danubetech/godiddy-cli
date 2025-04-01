package com.godiddy.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Version {

    private static final String VERSION_FILE = "version.properties";
    public static final String VERSION;

    static {
        String version;
        try (InputStream is = Version.class.getClassLoader().getResourceAsStream(VERSION_FILE)) {
            if (is == null) {
                throw new RuntimeException("Version properties file not found: " + VERSION_FILE);
            }

            Properties props = new Properties();
            props.load(is);

            version = props.getProperty("version");
            if (version == null || version.isEmpty()) {
                throw new RuntimeException("Version not defined in properties file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load version properties", e);
        }

        VERSION = version;
    }

    private Version() {
        // Prevent instantiation
    }
}