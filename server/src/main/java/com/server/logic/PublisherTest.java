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

public class PublisherTest implements Runnable {

    @Override
    public void run() {
        try (ZContext contextPUB = new ZContext()) {
            ZMQ.Socket publisherSocket = contextPUB.createSocket(SocketType.PUB);
            publisherSocket.bind(ADDRESSPUBSUB);

            ZMsg msg = new ZMsg();

            // Wait until the actors are ready to receive messages
            while (!ActorManager.isReady()) {
                msg.add(CONFIRMATION);
                msg.add(CONFIRMATION);
                msg.send(publisherSocket);
                System.out.println("[PUBLISHER " + Thread.currentThread() + "]: Waiting for comprobant to be ready");
            }

            // Publish 11 messages to the renewal and return topics
            for (int i = 0; i < 11; i++) {
                String topic = i % 2 == 0 ? RENEWALTOPIC : RETURNTOPIC;
                String message = "Number " + i;

                msg.clear();
                msg.add(topic);
                msg.add(message);
                msg.send(publisherSocket);

                System.out.println("[PUBLISHER " + Thread.currentThread() + "]: Published message '" + message + "'");
            }
            contextPUB.destroy();
        } catch (Exception e) {
            System.err.println("[PUBLISHER " + Thread.currentThread() + "] error: " + e.getMessage());
        }
    }
}
