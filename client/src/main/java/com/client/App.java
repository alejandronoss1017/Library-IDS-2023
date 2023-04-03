package com.client;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;


//
//  Hello World client in Java
//  Connects REQ socket to tcp://localhost:5555
//  Sends "Hello" to server, expects "World" back
//
public class App {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // Socket to talk to server
            System.out.println("Connecting to library server");

            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");

            for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
                String request = String.valueOf(requestNbr);
                System.out.println("Sending: " + requestNbr);
                socket.send(request.getBytes(ZMQ.CHARSET), 0);

                byte[] reply = socket.recv(0);
                System.out.println(
                        "Received " + new String(reply, ZMQ.CHARSET) + " from library server");
            }
        }
    }
}
