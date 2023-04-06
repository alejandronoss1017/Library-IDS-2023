package com.server.logic;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static com.server.ServerInfo.*;

public class ActorRenewal implements Runnable {
    private boolean status = false;

    @Override
    public void run() {
        try (ZContext contextSUB = new ZContext()) {
            // Create a subscriber socket
            ZMQ.Socket subscriberSocket = contextSUB.createSocket(SocketType.SUB);

            // Connect the subscriber socket to the publisher endpoint
            subscriberSocket.connect(ADDRESSPUBSUB);

            // Subscribe to messages with the topic "renewal" and "confirmation"
            subscriberSocket.subscribe(RENEWALTOPIC.getBytes(ZMQ.CHARSET));
            subscriberSocket.subscribe(CONFIRMATION.getBytes(ZMQ.CHARSET));

            // Listen for incoming messages
            while (!Thread.currentThread().isInterrupted()) {
                // Receive a message from the socket
                subscriberSocket.recvStr();

                // Check if the actor status is false
                if (!status) {

                    // Check if the received message is a confirmation message
                    if (subscriberSocket.recvStr().equals(CONFIRMATION)) {
                        status = true;
                        subscriberSocket.unsubscribe(CONFIRMATION.getBytes(ZMQ.CHARSET));
                    }
                } else {

                    // Receive the message and print it
                    String message = subscriberSocket.recvStr();
                    System.out.printf("[SUSCRIBER " + RENEWALTOPIC + " " + Thread.currentThread() + "] Received message '%s'%n", message);
                }
            }

            // Destroy the ZMQ context
            contextSUB.destroy();
        } catch (Exception e) {
            // Handle exceptions
            if (e.getMessage().equals("Errno 4")) {
                System.out.println("[" + RENEWALTOPIC + " " + Thread.currentThread() + "]: Hilo interrumpido.");
                status = false;
            } else {
                System.err.println("[" + RENEWALTOPIC + " " + Thread.currentThread() + "]: " + e.getMessage());
            }
        }
    }

    // Get the actor status
    public boolean isStatus() {
        return status;
    }
}
