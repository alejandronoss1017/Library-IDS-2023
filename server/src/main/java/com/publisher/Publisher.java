package com.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMsg;

import io.github.cdimascio.dotenv.Dotenv;

public class Publisher {
    private static final String PULL_PORT = Dotenv.load().get("PUSH_PULL_PORT");
    private static final String PULL_IP = Dotenv.load().get("PULL_IP");
    private static final String PUB_PORT = Dotenv.load().get("PUB_SUB_PORT");
    private static final String[] TOPICS = Dotenv.load().get("TOPICS").split(",");
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
    private static MessageQueue messageQueue = MessageQueue.getInstance();

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            // Socket to get messages from server

            // This must be a connect, not a bind, because the push is the one that
            // binds to the port and the pull connects to it.
            ZMQ.Socket pullSocket = initSocket(context, SocketType.PULL, PULL_IP, PULL_PORT, false);

            // Socket to send messages to subscribers
            ZMQ.Socket pubSocket = initSocket(context, SocketType.PUB, "*", PUB_PORT, true);

            logger.info("Push/Pull running on port " + PULL_PORT);
            logger.info("Publisher/Subscriber running on port " + PUB_PORT);
            
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("Waiting for messages...");

                ZMsg msg = ZMsg.recvMsg(pullSocket);

                messageQueue.produce(msg);

                logger.info("Message received from server: " + msg);

                // The first frame is the topic
                String topic = msg.popString();

                // The second frame is the message
                String message = msg.popString();

                // the third frame is the campus
                String campus = msg.popString();

                // Check if the topic matches the specific topic you want to send a message to
                if (isTopicMatch(topic, "Renewal")) {
                    // Create a new ZMsg to send the message to the subscriber
                    ZMsg pubMsg = new ZMsg();

                    // Set the topic
                    pubMsg.addString(topic);

                    // Set the message
                    pubMsg.addString(message);

                    // Set the campus
                    pubMsg.addString(campus);

                    // Send the message to the subscriber
                    pubMsg.send(pubSocket);
                }

            }

        } catch (ZMQException e) {
            if (e.getMessage().contains("Errno 48")) {
                logger.error("Address already in use.");
            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }

    }

    public static ZMQ.Socket initSocket(ZContext context, SocketType type, String ip, String port, boolean bind) {
        ZMQ.Socket socket = null;

        try {
            socket = context.createSocket(type);

            if (bind) {
                socket.bind("tcp://" + ip + ":" + port);
            } else {
                socket.connect("tcp://" + ip + ":" + port);
            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }

        return socket;
    }

    private static boolean isTopicMatch(String topic, String specificTopic) {
        // Perform the necessary comparison to check if the topic matches the specific
        // topic.
        // You can modify this logic based on your requirements.
        return topic.equals(specificTopic);
    }
}
