package com.godiddy.cli.clistorage;

import java.io.*;

public class CLIStorage {

    private static final String BASE_PATH = "./godiddy-cli-storage/";

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
