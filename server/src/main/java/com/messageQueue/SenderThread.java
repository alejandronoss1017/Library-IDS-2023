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
    private static final String PUSH_FROM_QUEUE_TO_PUB_PORT = Dotenv.load().get("PUSH_FROM_QUEUE_TO_PUB_PORT");
    private static final String PUSH_FROM_QUEUE_TO_PUB_IP = Dotenv.load().get("PUSH_FROM_QUEUE_TO_PUB_IP");
    private static final Logger logger = LoggerFactory.getLogger(SenderThread.class);

    public SenderThread(MessageQueue queue) {
        SenderThread.queue = queue;
    }

    @Override
    public void run() {
        try (ZContext pushContext = new ZContext()) {

            ZMQ.Socket pushSocket = SocketUtil.connectSocket(pushContext, SocketType.PUSH, PUSH_FROM_QUEUE_TO_PUB_IP,
                    PUSH_FROM_QUEUE_TO_PUB_PORT, true);

            logger.info("Sender connected to port " + PUSH_FROM_QUEUE_TO_PUB_PORT);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ZMsg msg = queue.consume();

                    logger.info("Message sended from the queue: " + msg.toString());
                    MessageUtil.sendMessage(pushSocket, msg);

                    msg.clear();
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
