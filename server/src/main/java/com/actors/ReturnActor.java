package com.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import io.github.cdimascio.dotenv.Dotenv;

import com.messageQueue.MessageQueue;
import com.utils.SocketUtil;

public class ReturnActor {
    private static final Logger logger = LoggerFactory.getLogger(ReturnActor.class);
    private static final String PUBLISHER_IP = Dotenv.load().get("PUBLISHER_IP");
    private static final String PUBLISHER_PORT = Dotenv.load().get("PUB_SUB_PORT");
    private static final String TOPIC = Dotenv.load().get("RETURN_TOPIC");
    private static MessageQueue returnWorkQueue = MessageQueue.getInstance();

    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {

            ZMQ.Socket subscriber = SocketUtil.connectSocket(context, SocketType.SUB, PUBLISHER_IP, PUBLISHER_PORT,
                    false);

            subscriber.subscribe(TOPIC);

            logger.info("Return actor listening on port " + PUBLISHER_PORT + " ...");

            // Start a thread to do the work of the actor
            Thread returnThread = new Thread(new DoWorkThread(returnWorkQueue));
            returnThread.start();

            while (!Thread.currentThread().isInterrupted()) {

                ZMsg msg = ZMsg.recvMsg(subscriber);

                // Checks if the message is for this actor
                if (msg.size() > 0 && msg.getFirst().toString().equals(TOPIC)) {
                    // Add the message to the queue
                    returnWorkQueue.produce(msg);

                    logger.info("Return actor received: " + msg.toString());
                }

            }

            subscriber.close();
            context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
