package com.server.logic;

import com.server.model.Actor;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ActorReturn implements Actor, Runnable {
    private final ZContext context = new ZContext();
    private final String topic = "Return";

    @Override
    public void execute() {
        ZMQ.Socket subscriberSocket = context.createSocket(SocketType.SUB);
        subscriberSocket.connect("tcp://localhost:5556");
        subscriberSocket.subscribe(topic.getBytes(ZMQ.CHARSET));

        while (!Thread.currentThread().isInterrupted()) {
            String message = subscriberSocket.recvStr();
            System.out.println("Received " + message + " from " + topic);
        }
    }

    @Override
    public void run() {
        execute();
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    public ZContext getContext() {
        return context;
    }

    public String getTopic() {
        return topic;
    }
}
