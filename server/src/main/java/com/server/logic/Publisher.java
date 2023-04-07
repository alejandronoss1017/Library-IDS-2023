package com.server.logic;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import static com.server.ServerInfo.*;


/*
 * This is the publisher class. It will send messages to the subscribers (actors -> renewal and return)
 * It will send a confirmation message to the subscribers to confirm that they are ready to receive messages
 * Is an example.
 */

public class Publisher implements Runnable {

    @Override
    public void run() {
        try (ZContext contextPUB = new ZContext()) {

            Buffer buffer = Buffer.getInstance();

            ZMQ.Socket publisherSocket = contextPUB.createSocket(SocketType.PUB);
            publisherSocket.bind(ADDRESSPUBSUB);

            ZMsg msg = new ZMsg();

            // Wait until the actors are ready to receive messages
            System.out.println("[PUBLISHER " + Thread.currentThread() + "]: Waiting for comprobant to be ready");
            while (!ActorManager.isReady()) {
                msg.add(CONFIRMATION);
                msg.add(CONFIRMATION);
                msg.send(publisherSocket);
                msg.clear();
            }
            System.out.println("[PUBLISHER " + Thread.currentThread() + "]: Actors ready");

            while (!Thread.currentThread().isInterrupted()) {
                msg = buffer.consume();
                System.out.println("Publisher: " + msg.toString());
                // msg.add(msg.getFirst().toString());
                msg.send(publisherSocket);
                System.out.println("[PUBLISHER " + Thread.currentThread() + "]: Message sent");
                Thread.sleep(1000);
                msg.clear();
            }
            contextPUB.destroy();
        } catch (Exception e) {
            System.err.println("[PUBLISHER " + Thread.currentThread() + "] error: " + e.getMessage());
        }
    }

}
