package com.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class App {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            System.out.println("Initializing server...");

            // Socket to listen for incoming requests
            ZMQ.Socket responder = context.createSocket(SocketType.REP);
            responder.bind("tcp://*:5555");

            System.out.println("Server running on port 5555");

            while (!Thread.currentThread().isInterrupted()) {
                // Wait for next request from client
                String request = responder.recvStr(0).trim();
                System.out.println("Received request: " + request);

                // Do some work
                String reply = "Hello, " + request + "!";
                Thread.sleep(1000);

                // Send reply back to client
                responder.send(reply.getBytes(ZMQ.CHARSET), 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

