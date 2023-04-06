package com.client;

import logic.menu;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;


public class App {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // Socket to talk to server
            System.out.println("Connecting to server...");
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://10.43.100.59:5555");

            System.out.println("Connection established");

            // while (condition) {
            // }

            /*
            menu getInMenu = new menu();
            getInMenu.main(args);
            */

            // Do 10 requests, waiting each time for a response
            for (int requestNbr = 0; requestNbr != 10; requestNbr++) {

                // Generate a random number between 1 and 2
                int randomNumber = (int) (Math.random() * 2) + 1;

                String request = Integer.toString(randomNumber);
                System.out.println("Sending request " + requestNbr + "...");
                socket.send(request.getBytes(ZMQ.CHARSET), 0);

                byte[] reply = socket.recv(0);
                System.out.println("Received reply " + requestNbr + " [" + new String(reply, ZMQ.CHARSET) + "]");
            }

        }

    }
}
