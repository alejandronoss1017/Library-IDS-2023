package com.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class ReturnActor {
    private static final Logger logger = LoggerFactory.getLogger(ReturnActor.class);

    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {

            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:7777");

            subscriber.subscribe("Return");

            logger.info("Renewal listening on port 7777");

            while (!Thread.currentThread().isInterrupted()) {

                ZMsg msg = ZMsg.recvMsg(subscriber);

                logger.info("Renewal actor received: " + msg.toString());
            }

            subscriber.close();
            context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
