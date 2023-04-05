
package com.server.model;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 * LoadManager
 */
public class LoadManager implements Runnable {

    private Publisher publisher;
    private ZMQ.Socket receiveSocket;
    private final ZContext context = new ZContext();

    // Constructor for LoadManager
    public LoadManager() {
        this.receiveSocket = context.createSocket(SocketType.REP);
        this.publisher = new Publisher(context);

        receiveSocket.bind("tcp://*:5555");

        publisher.getPubSocket().bind("tcp://localhost:5556");
        publisher.getRequestSocket().connect("tcp://localhost:5557");
    }

    @Override
    public void run() {
        try {
            receiveRequest();
            System.out.println("LoadManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveRequest() throws InterruptedException {

        String response = null;
        String topic = null;

        // Wait for next request from client
        while (!Thread.currentThread().isInterrupted()) {

            // Receive request from client
            byte[] request = receiveSocket.recv(0);

            // Print the client request
            System.out.println("Request from the client: " + new String(request, ZMQ.CHARSET));

            if (new String(request, ZMQ.CHARSET).equals("1")) {
                topic = "Return";
                response = "Returning books";

                receiveSocket.send(response.getBytes(ZMQ.CHARSET), 0);

                System.out.println("Sending " + response + " to " + topic);
                publisher.getPubSocket().send(topic);

            } else if (new String(request, ZMQ.CHARSET).equals("2")) {
                topic = "Renewal";
                response = "Renewing books";

                receiveSocket.send(response.getBytes(ZMQ.CHARSET), 0);

                System.out.println("Sending " + response + " to " + topic);
                publisher.getPubSocket().send(topic);
            }

            // Wait for 1 second
            Thread.sleep(1000);
        }
    }

}
