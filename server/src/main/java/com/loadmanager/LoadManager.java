package com.loadmanager;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadManager {

    private static final String LOAD_MANAGER_REPLY_PORT = Dotenv.load().get("LOAD_MANAGER_REPLY_PORT");
    private static final String PUSH_PORT = Dotenv.load().get("PUSH_PULL_PORT");
    private static final Logger logger = LoggerFactory.getLogger(LoadManager.class);

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            // Socket to talk to clients
            ZMQ.Socket replySocket = initSocket(context, SocketType.REP, "*", LOAD_MANAGER_REPLY_PORT, true);

            // Socket to talk to publisher
            ZMQ.Socket pushSocket = initSocket(context, SocketType.PUSH, "*", PUSH_PORT, true);

            while (!Thread.currentThread().isInterrupted()) {
                logger.info("Load Manager running on port " + LOAD_MANAGER_REPLY_PORT);
                logger.info("Push/Pull running on port " + PUSH_PORT);
                logger.info("Waiting for request from client...");

                ZMsg msg = new ZMsg();

                // Wait for next request from the client
                msg = ZMsg.recvMsg(replySocket);

                logger.info("Request received from the client: " + msg.toString());

                // If the request is a Borrow request, this will continue as a
                // request-reply pattern
                if (msg.getFirst().toString().equals("Borrow")) {

                } else {
                    forwardToPublisher(pushSocket, msg);
                    responseClient(replySocket);
                }

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
}
