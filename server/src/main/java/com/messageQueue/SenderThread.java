package com.messageQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import com.utils.MessageUtil;
import com.utils.SocketUtil;

import io.github.cdimascio.dotenv.Dotenv;

public class SenderThread implements Runnable {
    private static MessageQueue queue;

    private static final String REP_FROM_QUEUE_TO_PUB_PORT = Dotenv.load().get("REP_FROM_QUEUE_TO_PUB_PORT");

    private static final String REP_FROM_QUEUE_TO_PUB_IP = Dotenv.load().get("REP_FROM_QUEUE_TO_PUB_IP");

    private static final Logger logger = LoggerFactory.getLogger(SenderThread.class);

    public SenderThread(MessageQueue queue) {
        SenderThread.queue = queue;
    }

    @Override
    public void run() {
        try (ZContext replyContext = new ZContext()) {

            ZMQ.Socket replySocket = SocketUtil.connectSocket(replyContext, SocketType.REP,
                    REP_FROM_QUEUE_TO_PUB_IP, REP_FROM_QUEUE_TO_PUB_PORT, true);

            logger.info("Queue reply binding to port: " + REP_FROM_QUEUE_TO_PUB_PORT);

            while (!Thread.currentThread().isInterrupted()) {
                try {

                    ZMsg msg = new ZMsg();

                    // Wait for next request from the Publisher
                    msg = ZMsg.recvMsg(replySocket);

                    logger.info("Request received from Publisher: " + msg.toString());
                    msg.clear();

                    // Send reply back to Publisher when the queue is not empty
                    // otherwise wait for a message to be added to the queue.
                    msg = queue.consume();
                    logger.info("Message sended from the queue: " + msg.toString());
                    MessageUtil.sendMessage(replySocket, msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }

        throw new UnsupportedOperationException("Unimplemented method 'run'");

    }

}
