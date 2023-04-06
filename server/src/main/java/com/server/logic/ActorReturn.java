package com.server.logic;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static com.server.ServerInfo.*;

public class ActorReturn implements Runnable {
    private boolean status = false;

    @Override
    public void run() {
        try (ZContext contextSUB = new ZContext()) {
            // Create a ZeroMQ subscriber socket
            ZMQ.Socket subscriberSocket = contextSUB.createSocket(SocketType.SUB);

            // Connect the subscriber socket to the publisher address
            subscriberSocket.connect(ADDRESSPUBSUB);

            // Subscribe to the RETURN and CONFIRMATION topics
            subscriberSocket.subscribe(RETURNTOPIC.getBytes(ZMQ.CHARSET));
            subscriberSocket.subscribe(CONFIRMATION.getBytes(ZMQ.CHARSET));

            while (!Thread.currentThread().isInterrupted()) {
                // Receive a message from the subscriber socket
                subscriberSocket.recvStr();

                // If the actor is not ready to process messages, wait for a CONFIRMATION message
                if (!status) {
                    if (subscriberSocket.recvStr().equals(CONFIRMATION)) {
                        // Set the status to true once the CONFIRMATION message is received
                        status = true;
                        // Unsubscribe from the CONFIRMATION topic
                        subscriberSocket.unsubscribe(CONFIRMATION.getBytes(ZMQ.CHARSET));
                    }
                } else {
                    // If the actor is ready, process the received message
                    String message = subscriberSocket.recvStr();
                    System.out.printf("[SUSCRIBER " + RETURNTOPIC + " " + Thread.currentThread() + "] Received message '%s'%n", message);
                }
            }
            contextSUB.destroy();
        } catch (Exception e) {
            // Handle exceptions and set the status to false if the thread is interrupted
            if(e.getMessage().equals("Errno 4")) {
                System.out.println("[" + RETURNTOPIC + " " + Thread.currentThread() + "]: Hilo interrumpido.");
                status = false;
            }
            else
                System.err.println("[" + RETURNTOPIC + " " + Thread.currentThread() + "]: " + e.getMessage());
        }
    }

    public boolean isStatus() {
        return status;
    }
}
