
package com.server.model;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import com.server.logic.ActorManager;
import com.server.logic.Buffer;
import com.server.logic.Publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.server.ServerInfo.*;

/**
 * LoadManager
 */
public class LoadManager {

    private Publisher publisher;
    private ZMQ.Socket replySocket;
    private final ZContext context = new ZContext();

    private static final Logger logger = LoggerFactory.getLogger(LoadManager.class);

    // Constructor for LoadManager
    public LoadManager() {
        this.replySocket = context.createSocket(SocketType.REP);
        this.publisher = new Publisher();

        replySocket.bind("tcp://*:5555");
    }

    public void receiveRequest() throws InterruptedException {

        ActorManager.setUpActors();

        Thread publisherThread = new Thread(publisher);
        publisherThread.start();

        ZMsg msg = new ZMsg();

        // Wait for next request from client
        logger.debug("Waiting for request from client...");

        Buffer buffer = Buffer.getInstance();

        while (true) {

            // Receive request from client
            msg = ZMsg.recvMsg(replySocket);

            // Print the client request
            System.out.println("================================================================");
            System.out.println("Request received from the client: " + msg.toString());

            if (msg.getFirst().toString().equalsIgnoreCase(RETURNTOPIC)) {

                ZMsg msgToPub = new ZMsg();

                msgToPub.add(RETURNTOPIC);
                msgToPub.add(msg.getLast().toString());

                System.out.println("Sending return request to publisher " + msg.toString());

                // Add message to buffer to be sent to publisher
                buffer.produce(msgToPub);

                // Send reply back to client
                msg.clear();
                msg.add("OK");
                System.out.println("Sending reply back to client " + msg.toString());
                System.out.println("================================================================");

                msg.send(replySocket);
                msg.clear();
            } else if (msg.getFirst().toString().equalsIgnoreCase(RENEWALTOPIC)) {

                System.out.println("Sending renewal request to publisher " + msg.toString());

                ZMsg msgToPub = new ZMsg();

                msgToPub.add(RENEWALTOPIC);
                msgToPub.add(msg.getLast().toString());

                System.out.println("Sending return request to publisher " + msg.toString());

                // Add message to buffer to be sent to publisher
                buffer.produce(msgToPub);

                // Send reply back to client
                msg.clear();
                msg.add("OK");
                System.out.println("Sending reply back to client " + msg.toString());
                System.out.println("================================================================");

                msg.send(replySocket);
                msg.clear();
            }
        }
    }

}
