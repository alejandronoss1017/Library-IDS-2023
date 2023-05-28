package com.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMsg;

import com.messageQueue.MessageQueue;

public class DoWorkThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DoWorkThread.class);
    private MessageQueue messageQueue;

    public DoWorkThread(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            try {
                ZMsg msg = messageQueue.consume();
                // Do database work
                logger.info("Doing database stuff...");
                System.out.println("Message working on: " + msg.toString());
            } catch (InterruptedException e) {
                logger.error("Error: " + e.getMessage());
                Thread.currentThread().interrupt();

            }
        }
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
