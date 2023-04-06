package com.server;

// Interface defining constants used in the server
public interface ServerInfo {
    // Topic for renewals
    String RENEWALTOPIC = "Renewal";
    // Topic for returns
    String RETURNTOPIC = "Return";
    // Port for PUB/SUB communication
    String PORTPUBSUB = "51151";
    // Address for PUB/SUB communication
    String ADDRESSPUBSUB = "tcp://localhost:" + PORTPUBSUB;
    // Confirmation message
    String CONFIRMATION = "Confirmation";
}
