package com.messageQueue;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import io.github.cdimascio.dotenv.Dotenv;

public class SenderThread implements Runnable {
    private static MessageQueue queue;
    private static final String PUSH_FROM_QUEUE_TO_PUB_PORT = Dotenv.load().get("PUSH_FROM_QUEUE_TO_PUB_PORT");
    private static final String PUSH_FROM_QUEUE_TO_PUB_IP = Dotenv.load().get("PUSH_FROM_QUEUE_TO_PUB_IP");

    public SenderThread(MessageQueue queue) {
        SenderThread.queue = queue;
    }

    @Override
    public void run() {
        try (ZContext pushContext = new ZContext()) {

            ZMQ.Socket pushSocket = pushContext.createSocket(SocketType.PUSH);

            pushSocket.bind("tcp://" + PUSH_FROM_QUEUE_TO_PUB_IP + ":" + PUSH_FROM_QUEUE_TO_PUB_PORT);

            System.out.println("Sender connected to port " + PUSH_FROM_QUEUE_TO_PUB_PORT);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ZMsg msg = queue.consume();

                    System.out.println("Message sended from the queue: " + msg.toString());
                    msg.send(pushSocket);

                    msg.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        throw new UnsupportedOperationException("Unimplemented method 'run'");

    }

}
