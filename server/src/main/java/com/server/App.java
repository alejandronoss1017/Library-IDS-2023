package com.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.logic.ProcessLogic;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static Process loadManagerProcess = ProcessLogic.startProcess("java", "-jar",
            Dotenv.load().get("LOAD_MANAGER_JAR"));

    public static void main(String[] args) throws Exception {

        // Add a shutdown hook to destroy the main process when the server is shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down server...");
        }));

        logger.info("Initializing server...");

        // Add a shutdown hook to destroy the loadManagerProcess when the server is
        // shutdown
        ProcessLogic.addShutdownHook(loadManagerProcess);

        // Wait for the loadManagerProcess to start
        if (ProcessLogic.waitForProcess(loadManagerProcess) && loadManagerProcess != null) {
            logger.info("Load Manager started successfully");
            logger.info("Load Manager PID: " + loadManagerProcess.pid());

        } else {
            logger.error("Load Manager failed to start");
            return;
        }

        // Start a thread to print the output from the loadManagerProcess

        while (true) {

            if (!loadManagerProcess.isAlive()) {
                logger.error("Load Manager process is not running");

                ProcessLogic.stopProcess(loadManagerProcess);

                logger.info("Attempting to restart Load Manager process...");
                loadManagerProcess = ProcessLogic.startProcess("java", "-jar",
                        Dotenv.load().get("LOAD_MANAGER_JAR"));
                ProcessLogic.addShutdownHook(loadManagerProcess);

                if (ProcessLogic.waitForProcess(loadManagerProcess)) {
                    logger.info("Load Manager restarted successfully");
                    logger.info("Load Manager PID: " + loadManagerProcess.pid());
                } else {
                    logger.error("Load Manager failed to restart");
                    return;
                }
            }

            Thread.sleep(1000);
        }

    }
}
