package com.server;

public interface ServerInfo {
    String RENEWALTOPIC = "Renewal";
    String RETURNTOPIC = "Return";
    String PORTPUBSUB = "51151";
    String ADDRESSPUBSUB = "tcp://localhost:" + PORTPUBSUB;
    String CONFIRMATION = "Confirmation";
}
