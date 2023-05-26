package com.server.logic;

import java.io.IOException;

import io.github.cdimascio.dotenv.Dotenv;

public class ProcessLogic {

    public static final int MAX_ATTEMPTS = Integer.parseInt(Dotenv.load().get("MAX_ATTEMPTS"));
    public static final int DELAY = Integer.parseInt(Dotenv.load().get("DELAY"));

    // Add a shutdown hook to destroy the a process when the server is shut
    // down
    public static void addShutdownHook(Process process) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            process.destroy();
            if (process != null) {
                process.destroy();
            }
        }));
    }

    // Start a process and return the process
    public static Process startProcess(String... command) {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            return processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    // Wait for a process to start
    public static boolean waitForProcess(Process process) {
        int attempts = 0;
        while (process == null && attempts < MAX_ATTEMPTS) {
            try {
                Thread.sleep(DELAY);
                attempts++;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return attempts < MAX_ATTEMPTS;
    }

    // Stop a process
    public static void stopProcess(Process process) {
        if (process != null && process.isAlive()) {
            process.destroy();

            // Wait until the process is completely stopped
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
