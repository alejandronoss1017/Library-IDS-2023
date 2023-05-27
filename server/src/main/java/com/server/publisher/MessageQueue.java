package com.server.publisher;

import java.util.LinkedList;
import java.util.Queue;


import org.zeromq.ZMsg;


public class MessageQueue {
    private static MessageQueue instance = null;
    private final Queue<ZMsg> queue = new LinkedList<ZMsg>();

    public static MessageQueue getInstance() {
        if (instance == null) {
            instance = new MessageQueue();
        }
        return instance;
    }

    private MessageQueue() {
    }

    public synchronized void produce(ZMsg msg) {
        queue.add(msg);
        notifyAll();
    }

    public synchronized ZMsg consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }

}
