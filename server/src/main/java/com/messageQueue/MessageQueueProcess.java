package com.messageQueue;


public class MessageQueueProcess {
    public static void main(String[] args) {
        MessageQueue queue = MessageQueue.getInstance();

        Thread receiverThread = new Thread(new ReceiverThread(queue));
        receiverThread.start();

        Thread senderThread = new Thread(new SenderThread(queue));
        senderThread.start();

        try {
            receiverThread.join();
            senderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
