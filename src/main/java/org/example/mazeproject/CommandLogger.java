package org.example.mazeproject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommandLogger {
    private static final String LOG_FILE = "commands.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String actionName) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            writer.write(actionName + " (" + timestamp + ")\n");
        } catch (IOException e) {
            CommandLogger.error("Writing to file", e);
        }
    }

    public static void error(String context, Exception e) {
        System.err.println("[ERROR]" + context + ": " + e.getMessage());
    }
}
