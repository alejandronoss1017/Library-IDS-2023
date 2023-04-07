package com.server;

import com.server.model.LoadManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        logger.info("Initializing server...");

        LoadManager loadManager = new LoadManager();

        logger.info("Server running for Request-Reply on port 5555");
        loadManager.receiveRequest();
    }
}
