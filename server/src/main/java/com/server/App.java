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

    private static Process queueProcess = ProcessLogic.startProcess("java", "-jar",
            Dotenv.load().get("QUEUE_JAR"));

    private static Process borrowActorProcess = ProcessLogic.startProcess("java", "-jar",
            Dotenv.load().get("BORROW_ACTOR_JAR"));

    private static Process renewalActorProcess = ProcessLogic.startProcess("java", "-jar",
            Dotenv.load().get("RENEWAL_ACTOR_JAR"));

    private static Process returnActorProcess = ProcessLogic.startProcess("java", "-jar",
            Dotenv.load().get("RETURN_ACTOR_JAR"));

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

            if (!queueProcess.isAlive()) {
                logger.error("Queue process is not running");

                ProcessLogic.stopProcess(queueProcess);

                logger.info("Attempting to restart Queue process...");
                queueProcess = ProcessLogic.startProcess("java", "-jar", Dotenv.load().get("QUEUE_JAR"));
                ProcessLogic.addShutdownHook(queueProcess);

                if (ProcessLogic.waitForProcess(queueProcess)) {
                    logger.info("Queue restarted successfully");
                    logger.info("Queue PID: " + queueProcess.pid());
                } else {
                    logger.error("Queue failed to restart");
                    return;
                }
            }

            if (!borrowActorProcess.isAlive()) {
                logger.error("Borrow Actor process is not running");

                ProcessLogic.stopProcess(borrowActorProcess);

                logger.info("Attempting to restart Borrow Actor process...");
                borrowActorProcess = ProcessLogic.startProcess();
                ProcessLogic.addShutdownHook(borrowActorProcess);

                if (ProcessLogic.waitForProcess(borrowActorProcess)) {
                    logger.info("Borrow Actor restarted successfully");
                    logger.info("Borrow Actor PID: " + borrowActorProcess.pid());
                } else {
                    logger.error("Borrow Actor failed to restart");
                    return;
                }

            }

            if (!renewalActorProcess.isAlive()) {
                logger.error("Renewal Actor process is not running");

                ProcessLogic.stopProcess(renewalActorProcess);

                logger.info("Attempting to restart Renewal Actor process...");
                renewalActorProcess = ProcessLogic.startProcess();
                ProcessLogic.addShutdownHook(renewalActorProcess);

                if (ProcessLogic.waitForProcess(renewalActorProcess)) {
                    logger.info("Renewal Actor restarted successfully");
                    logger.info("Renewal Actor PID: " + renewalActorProcess.pid());
                } else {
                    logger.error("Renewal Actor failed to restart");
                    return;
                }

            }

            if (!returnActorProcess.isAlive()) {
                logger.error("Return Actor process is not running");

                ProcessLogic.stopProcess(returnActorProcess);

                logger.info("Attempting to restart Return Actor process...");
                returnActorProcess = ProcessLogic.startProcess();
                ProcessLogic.addShutdownHook(returnActorProcess);

                if (ProcessLogic.waitForProcess(returnActorProcess)) {
                    logger.info("Return Actor restarted successfully");
                    logger.info("Return Actor PID: " + returnActorProcess.pid());
                } else {
                    logger.error("Return Actor failed to restart");
                    return;
                }

            }

            Thread.sleep(1000);
        }

    }

    public static void setShutdownHooks() {
        ProcessLogic.addShutdownHook(loadManagerProcess);
        ProcessLogic.addShutdownHook(publisherProcess);
        ProcessLogic.addShutdownHook(queueProcess);
        ProcessLogic.addShutdownHook(borrowActorProcess);
        ProcessLogic.addShutdownHook(renewalActorProcess);
        ProcessLogic.addShutdownHook(returnActorProcess);
    }

    public static boolean waitForProcesses() {

        boolean pLoadManager = ProcessLogic.waitForProcess(loadManagerProcess) && loadManagerProcess != null;
        boolean pPub = ProcessLogic.waitForProcess(publisherProcess) && publisherProcess != null;
        boolean pQueue = ProcessLogic.waitForProcess(queueProcess) && queueProcess != null;
        boolean pBorrow = ProcessLogic.waitForProcess(borrowActorProcess) && borrowActorProcess != null;
        boolean pRenewal = ProcessLogic.waitForProcess(renewalActorProcess) && renewalActorProcess != null;
        boolean pReturn = ProcessLogic.waitForProcess(returnActorProcess) && returnActorProcess != null;

        return pPub && pLoadManager && pQueue && pBorrow && pRenewal && pReturn;
    }

    public static void showProcessesInfo() {
        logger.info("Load Manager PID: " + loadManagerProcess.pid());
        logger.info("Publisher PID: " + publisherProcess.pid());
        logger.info("Queue PID: " + queueProcess.pid());
        logger.info("Borrow Actor PID: " + borrowActorProcess.pid());
        logger.info("Renewal Actor PID: " + renewalActorProcess.pid());
        logger.info("Return Actor PID: " + returnActorProcess.pid());

    }

}
