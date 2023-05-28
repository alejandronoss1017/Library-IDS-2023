package com.messageQueue;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import io.github.cdimascio.dotenv.Dotenv;

public class ReceiverThread implements Runnable {
    private static MessageQueue queue;
    private static final String PULL_FROM_LOADMANAGER_PORT = Dotenv.load().get("PULL_FROM_LOAD_MANAGER_TO_QUEUE_PORT");
    private static final String PULL_FROM_LOADMANAGER_IP = Dotenv.load().get("PULL_FROM_LOAD_MANAGER_TO_QUEUE_IP");

    public ReceiverThread(MessageQueue queue) {
        ReceiverThread.queue = queue;
    }

    @Override
    public void run() {

        try (ZContext pullContext = new ZContext()) {

            ZMQ.Socket pullSocket = pullContext.createSocket(SocketType.PULL);

            pullSocket.connect("tcp://" + PULL_FROM_LOADMANAGER_IP + ":" + PULL_FROM_LOADMANAGER_PORT);
            System.out.println("Receiver connected to port " + PULL_FROM_LOADMANAGER_PORT);

            while (!Thread.currentThread().isInterrupted()) {
                ZMsg receivedMsg = ZMsg.recvMsg(pullSocket);
                queue.produce(receivedMsg);
                System.out.println("Message added to queue: " + receivedMsg.toString());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
