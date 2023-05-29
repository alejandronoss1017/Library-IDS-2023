package com.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMsg;

import com.utils.SocketUtil;

import io.github.cdimascio.dotenv.Dotenv;

public class Publisher {
    private static final String REQ_FROM_PUB_TO_QUEUE_PORT = Dotenv.load().get("REQ_FROM_PUB_TO_QUEUE_PORT");

    private static final String REQ_FROM_PUB_TO_QUEUE_IP = Dotenv.load().get("REQ_FROM_PUB_TO_QUEUE_IP");

    private static final String PUB_PORT = Dotenv.load().get("PUB_SUB_PORT");

    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            // Socket to request a message from the queue
            ZMQ.Socket requestSocket = SocketUtil.connectSocket(context, SocketType.REQ,
                    REQ_FROM_PUB_TO_QUEUE_IP, REQ_FROM_PUB_TO_QUEUE_PORT, false);

            // Socket to send messages to subscribers
            ZMQ.Socket pubSocket = SocketUtil.connectSocket(context, SocketType.PUB, "*", PUB_PORT, true);

            logger.info("Req/Rep Queue running on port:  " + REQ_FROM_PUB_TO_QUEUE_PORT);
            logger.info("Publisher/Subscriber running on port " + PUB_PORT);

            logger.info("Waiting for messages...");

            while (!Thread.currentThread().isInterrupted()) {

                ZMsg msg = new ZMsg();

                msg.add("Send me a message");

                msg.send(requestSocket);
                msg.clear();

                // If the queue is empty, this will block until a message is received
                msg = ZMsg.recvMsg(requestSocket);

                logger.info("Message received from the queue: " + msg);

                // Send the message to subscribers of the topic
                msg.send(pubSocket);

            }

        } catch (ZMQException e) {
            if (e.getMessage().contains("Errno 48")) {
                logger.error("Address already in use.");
            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }

    }
}
