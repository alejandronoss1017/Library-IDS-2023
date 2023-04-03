package com.server.logic;

import com.server.model.Actor;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ActorReturn implements Actor, Runnable {

    @Override
    public void execute() {
        String topic = "Return", port = "5115";

        try (ZContext context = new ZContext()) {
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);

            subscriber.connect("tcp://localhost:" + port);
            subscriber.subscribe(topic.getBytes(ZMQ.CHARSET));

            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Thread " + topic + " is listening...");

                String returnTopic = subscriber.recvStr(0);

                String message = new String(subscriber.recv(0), ZMQ.CHARSET);

                /* Codigo */
                System.out.println("wdwdwd");
            }

            context.destroy();
            subscriber.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + " The Actor:" + topic + " can't be executed.");
        }
    }

    @Override
    public void run() {
        execute();
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
}
