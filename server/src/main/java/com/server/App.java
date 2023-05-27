package com.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.logic.ProcessLogic;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static Process loadManagerProcess = ProcessLogic.startProcess("java", "-jar",
            Dotenv.load().get("LOAD_MANAGER_JAR"));

    private static Process publisherProcess = ProcessLogic.startProcess("java", "-jar",
            Dotenv.load().get("PUBLISHER_JAR"));

    public static void main(String[] args) throws Exception {

        // Add a shutdown hook to destroy the main process when the server is shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down server...");
        }));

        logger.info("Initializing server...");

        // Add a shutdown hooks to destroy the processes when the server is shutdown

        // Wait for all process to start
        if (waitForProcesses()) {
            showProcessesInfo();
        } else {
            logger.error("Server failed to start");
            return;
        }

        setShutdownHooks();
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

            if (!publisherProcess.isAlive()) {
                logger.error("Publisher process is not running");

                ProcessLogic.stopProcess(publisherProcess);

                logger.info("Attempting to restart Publisher process...");
                publisherProcess = ProcessLogic.startProcess("java", "-jar",
                        Dotenv.load().get("PUBLISHER_JAR"));
                ProcessLogic.addShutdownHook(publisherProcess);

                if (ProcessLogic.waitForProcess(publisherProcess)) {
                    logger.info("Publisher restarted successfully");
                    logger.info("Publisher PID: " + publisherProcess.pid());
                } else {
                    logger.error("Publisher failed to restart");
                    return;
                }
            }

            Thread.sleep(1000);
        }

    }

    public static void setShutdownHooks() {
        ProcessLogic.addShutdownHook(loadManagerProcess);
        ProcessLogic.addShutdownHook(publisherProcess);
    }

    public static boolean waitForProcesses() {

        boolean pLoadManager = ProcessLogic.waitForProcess(loadManagerProcess) && loadManagerProcess != null;
        boolean pPub = ProcessLogic.waitForProcess(publisherProcess) && publisherProcess != null;

        return pPub && pLoadManager;
    }

    public static void showProcessesInfo() {
        logger.info("Load Manager PID: " + loadManagerProcess.pid());
        logger.info("Publisher PID: " + publisherProcess.pid());
    }

}
