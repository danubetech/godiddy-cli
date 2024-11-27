package com.godiddy.cli.clidata;

import com.godiddy.cli.GodiddyCLIApplication;

import java.io.*;
import java.util.prefs.Preferences;

public class CLIData {

    private static final Preferences preferences = Preferences.userNodeForPackage(GodiddyCLIApplication.class);
    private static final String BASE_PATH = "./godiddy-cli-data/";

    public static String get(String key) {
        File file = new File(BASE_PATH, key);
        if (! file.exists()) return null;
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static void put(String key, String value) {
        File parent = new File(BASE_PATH);
        if (! parent.exists()) parent.mkdirs();
        File file = new File(parent, key);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(value + "\n");
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static void remove(String key) {
        File file = new File(BASE_PATH, key);
        if (file.exists()) file.delete();
    }
}
