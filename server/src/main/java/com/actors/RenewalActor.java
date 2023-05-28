package com.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import com.messageQueue.MessageQueue;
import com.utils.SocketUtil;

import io.github.cdimascio.dotenv.Dotenv;

public class RenewalActor {
    private static final Logger logger = LoggerFactory.getLogger(RenewalActor.class);
    private static final String PUBLISHER_IP = Dotenv.load().get("PUBLISHER_IP");
    private static final String PUBLISHER_PORT = Dotenv.load().get("PUB_SUB_PORT");
    private static final String TOPIC = Dotenv.load().get("RENEWAL_TOPIC");
    private static MessageQueue renewalWorkQueue = MessageQueue.getInstance();

    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {

            ZMQ.Socket subscriber = SocketUtil.connectSocket(context, SocketType.SUB, PUBLISHER_IP, PUBLISHER_PORT,
                    false);

            subscriber.subscribe(TOPIC);

            logger.info("Renewal listening on port " + PUBLISHER_PORT + " ...");

            // Start a thread to do the work of the actor
            Thread renewalThread = new Thread(new DoWorkThread(renewalWorkQueue));
            renewalThread.start();

            while (!Thread.currentThread().isInterrupted()) {

                ZMsg msg = ZMsg.recvMsg(subscriber);

                // Notify the thread that there is work to do and pass it the message.
                renewalWorkQueue.produce(msg);

                logger.info("Renewal actor received: " + msg.toString());
            }

            subscriber.close();
            context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
