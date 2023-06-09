package com.loadManager;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMsg;

import com.utils.MessageUtil;
import com.utils.SocketUtil;

import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadManager {

    private static final String LOAD_MANAGER_REPLY_PORT = Dotenv.load().get("LOAD_MANAGER_REPLY_PORT");

    private static final String LOAD_MANAGER_TO_ACTOR_REQUEST_PORT = Dotenv.load()
            .get("LOAD_MANAGER_TO_ACTOR_REQUEST_PORT");

    private static final String LOAD_MANAGER_TO_ACTOR_REQUEST_IP = Dotenv.load()
            .get("LOAD_MANAGER_TO_ACTOR_REQUEST_IP");

    private static final String PUSH_PORT_QUEUE = Dotenv.load().get("PUSH_FROM_LOAD_MANAGER_TO_QUEUE_PORT");

    private static final String TIMEOUT = Dotenv.load().get("TIMEOUT");

    private static final Logger logger = LoggerFactory.getLogger(LoadManager.class);

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            // Socket to talk to clients
            ZMQ.Socket replySocket = SocketUtil.connectSocket(context, SocketType.REP, "*", LOAD_MANAGER_REPLY_PORT,
                    true);

            // Socket to talk to the push/pull queue
            ZMQ.Socket pushSocket = SocketUtil.connectSocket(context, SocketType.PUSH, "*", PUSH_PORT_QUEUE, true);

            // Socket to talk to the Borrow Actor
            ZMQ.Socket requestSocket = SocketUtil.connectSocket(context, SocketType.REQ,
                    LOAD_MANAGER_TO_ACTOR_REQUEST_IP,
                    LOAD_MANAGER_TO_ACTOR_REQUEST_PORT, false);

            // Set the timeout for the request socket to borrow actor
            requestSocket.setReceiveTimeOut(Integer.parseInt(TIMEOUT));

            // This is printed once the Load Manager is running
            logger.info("Load Manager running on port " + LOAD_MANAGER_REPLY_PORT);
            logger.info("Push/Pull QUEUE running on port " + PUSH_PORT_QUEUE);

            logger.info("Waiting for request from client...");

            while (!Thread.currentThread().isInterrupted()) {

                ZMsg msg = new ZMsg();

                // Wait for next request from the client
                msg = ZMsg.recvMsg(replySocket);

                logger.info("Request received from the client: " + msg.toString());

                // If the request is a Borrow request, this will continue as a
                // request-reply pattern
                if (msg.getFirst().toString().equals("Borrow")) {

                    // Send the request to the Borrow Actor
                    logger.info("Sending request to the Borrow Actor...");
                    MessageUtil.sendMessage(requestSocket, msg);

                    try {
                        // Wait for the reply from the Borrow Actor
                        msg = ZMsg.recvMsg(requestSocket);

                        if (msg != null) {

                            // Send the reply back to the client
                            logger.info("Sending reply back to client...");

                            MessageUtil.sendMessage(replySocket, msg);
                        } else {
                            logger.error("Cannot get a response in " + TIMEOUT + " ms from the Borrow Actor");
                            MessageUtil.sendMessage(replySocket, "Reach timeout, try again later");

                        }

                    } catch (ZMQException e) {

                        if (e.getErrorCode() == ZMQ.Error.ETERM.getCode()) {

                            // The connection has been terminated, try to change the connection
                            logger.error("Cannot get a response in " + TIMEOUT + " ms");

                        } else if (e.getErrorCode() == ZMQ.Error.EFSM.getCode()) {

                            // The connection is not established, try to change the connection
                            logger.error("Cannot get a response in " + TIMEOUT + " ms");

                        } else {

                            // Another type of ZMQ.Error, print the message and exit the loop
                            System.out.println("Error: " + e.getMessage());
                            break;
                        }
                    }

                } else {
                    // Send a reply back to the client
                    logger.info("Sending reply back to client...");
                    MessageUtil.sendMessage(replySocket, "OK");

                    // Send the message to the push/pull queue
                    logger.info("Sending message to the queue...");
                    MessageUtil.sendMessage(pushSocket, msg);

                }

            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }
}
