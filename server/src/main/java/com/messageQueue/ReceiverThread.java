package com.messageQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import com.utils.SocketUtil;

import io.github.cdimascio.dotenv.Dotenv;

public class ReceiverThread implements Runnable {
    private static MessageQueue queue;
    private static final String PULL_FROM_LOADMANAGER_PORT = Dotenv.load().get("PULL_FROM_LOAD_MANAGER_TO_QUEUE_PORT");
    private static final String PULL_FROM_LOADMANAGER_IP = Dotenv.load().get("PULL_FROM_LOAD_MANAGER_TO_QUEUE_IP");
    private static final Logger logger = LoggerFactory.getLogger(ReceiverThread.class);

    public ReceiverThread(MessageQueue queue) {
        ReceiverThread.queue = queue;
    }

    @Override
    public void run() {

        try (ZContext pullContext = new ZContext()) {

            ZMQ.Socket pullSocket = SocketUtil.connectSocket(pullContext, SocketType.PULL, PULL_FROM_LOADMANAGER_IP,
                    PULL_FROM_LOADMANAGER_PORT, false);

            logger.info("Receiver connected to port " + PULL_FROM_LOADMANAGER_PORT);

            while (!Thread.currentThread().isInterrupted()) {
                ZMsg receivedMsg = ZMsg.recvMsg(pullSocket);
                logger.info("Message added to queue: " + receivedMsg.toString());
                queue.produce(receivedMsg);
            }

        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }

        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
