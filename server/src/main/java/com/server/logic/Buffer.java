package com.server.logic;

import java.util.LinkedList;
import java.util.Queue;

import org.zeromq.ZMsg;

public class Buffer {
    private static Buffer instance = null;
    private final Queue<ZMsg> queue = new LinkedList<ZMsg>();

    public static Buffer getInstance(){
        if (instance == null) {
            instance = new Buffer();
        }
        return instance;
    }

    private Buffer() {
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
