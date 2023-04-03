package com.server.logic;

import com.server.model.Actor;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class ActorRenewal implements Actor {
    @Override
    public void execute() {
        String topic = "Renewal", port = "51151";

        try(ZContext context = new ZContext()) {
            ZMQ.Socket subscriber = context.createSocket(ZMQ.SUB);

            subscriber.connect("tcp://localhost:" + port);
            subscriber.subscribe(topic.getBytes(ZMQ.CHARSET));

            while (!Thread.currentThread().isInterrupted()) {
                String message = new String(subscriber.recv(0), ZMQ.CHARSET);

                /* Codigo */
                System.out.println("edeefe");
            }

            context.destroy();
            subscriber.close();

        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + " The Actor:" + topic + " can't be executed.");
        }
    }
}
