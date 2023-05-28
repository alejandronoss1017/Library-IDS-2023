package com.loadmanager;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import com.utils.SocketUtil;

import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadManager {

    private static final String LOAD_MANAGER_REPLY_PORT = Dotenv.load().get("LOAD_MANAGER_REPLY_PORT");
    private static final String PUSH_PORT_QUEUE = Dotenv.load().get("PUSH_FROM_LOAD_MANAGER_TO_QUEUE_PORT");
    private static final Logger logger = LoggerFactory.getLogger(LoadManager.class);

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            // Socket to talk to clients
            ZMQ.Socket replySocket = SocketUtil.connectSocket(context, SocketType.REP, "*", LOAD_MANAGER_REPLY_PORT,
                    true);

            // Socket to talk to the push/pull queue
            ZMQ.Socket pushSocket = SocketUtil.connectSocket(context, SocketType.PUSH, "*", PUSH_PORT_QUEUE, true);

            // This is printed once the Load Manager is running
            logger.info("Load Manager running on port " + LOAD_MANAGER_REPLY_PORT);
            logger.info("Push/Pull QUEUE running on port " + PUSH_PORT_QUEUE);

            while (!Thread.currentThread().isInterrupted()) {
                logger.info("Waiting for request from client...");

                ZMsg msg = new ZMsg();

                // Wait for next request from the client
                msg = ZMsg.recvMsg(replySocket);

                logger.info("Request received from the client: " + msg.toString());

                // If the request is a Borrow request, this will continue as a
                // request-reply pattern
                if (msg.getFirst().toString().equals("Borrow")) {

                } else {
                    forwardToQueue(pushSocket, msg);
                    responseClient(replySocket);
                }

            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void responseClient(ZMQ.Socket socket) {
        ZMsg msg = new ZMsg();
        msg.add("OK");

        logger.info("Sending reply back to client: " + msg.toString());

        msg.send(socket);
        msg.clear();
    }

    public static void forwardToPublisher(ZMQ.Socket socket, ZMsg msg) {
        logger.info("Sending request to Publisher...");
        // Send message to Publisher
        msg.send(socket);
    }

    public static void forwardToQueue(ZMQ.Socket socket, ZMsg msg) {
        logger.info("Sending message to the queue...");
        msg.send(socket);
    }
}
