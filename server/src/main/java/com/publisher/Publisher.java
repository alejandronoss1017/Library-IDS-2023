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
    private static final String PULL_FROM_PUB_TO_QUEUE_PORT = Dotenv.load().get("PULL_FROM_PUB_TO_QUEUE_PORT");
    private static final String PULL_FROM_PUB_TO_QUEUE_IP = Dotenv.load().get("PULL_FROM_PUB_TO_QUEUE_IP");
    private static final String PUB_PORT = Dotenv.load().get("PUB_SUB_PORT");
    private static final String[] TOPICS = Dotenv.load().get("TOPICS").split(",");
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            // Socket to get messages from server

            // This must be a connect, not a bind, because the push is the one that
            // binds to the port and the pull connects to it.
            ZMQ.Socket pullSocket = SocketUtil.connectSocket(context, SocketType.PULL,
                    PULL_FROM_PUB_TO_QUEUE_IP, PULL_FROM_PUB_TO_QUEUE_PORT, false);

            // Socket to send messages to subscribers
            ZMQ.Socket pubSocket = SocketUtil.connectSocket(context, SocketType.PUB, "*", PUB_PORT, true);

            logger.info("Push/Pull Queue running on port " + PULL_FROM_PUB_TO_QUEUE_PORT);
            logger.info("Publisher/Subscriber running on port " + PUB_PORT);

            logger.info("Waiting for messages...");

            while (!Thread.currentThread().isInterrupted()) {

                ZMsg msg = ZMsg.recvMsg(pullSocket);

                // messageQueue.produce(msg);

                logger.info("Message received from server: " + msg);

                // The first frame is the topic
                String topic = msg.popString();

                // The second frame is the message
                String message = msg.popString();

                // the third frame is the campus
                String campus = msg.popString();

                // Send the message to subscribers of the topic
                pubSocket.sendMore(topic);
                pubSocket.sendMore(campus);
                pubSocket.send(message);

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
