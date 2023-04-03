package com.server;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import com.server.logic.InitActors;

import org.zeromq.ZContext;

public class App {
    public static void main(String[] args) throws Exception {
        try (ZContext context = new ZContext()) {


            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5555");

            InitActors.setUpActors();
            
            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = socket.recv(0);

                System.out.println(
                        "Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]");

                try (ZContext publisherContext = new ZContext()) {
                    ZMQ.Socket publisher = publisherContext.createSocket(SocketType.PUB);
                    publisher.bind("tcp://*:5115");
                    if (new String(reply, ZMQ.CHARSET).equals("1")) {
                        publisher.sendMore("Renewal");
                        publisher.send(new String(reply, ZMQ.CHARSET).getBytes(ZMQ.CHARSET), 0);
                    } else {
                        publisher.sendMore("Return");
                        publisher.send(new String(reply, ZMQ.CHARSET).getBytes(ZMQ.CHARSET), 0);
                    }
                }

                /* Code to execute */
                Thread.sleep(1000); // Do some 'work'

                String response = new String(reply, ZMQ.CHARSET);
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        }
    }
}
