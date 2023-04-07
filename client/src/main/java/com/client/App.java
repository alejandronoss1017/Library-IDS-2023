package com.client;

import logic.menu;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class App {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            // Socket to talk to server
            System.out.println("Connecting to server...");
            ZMQ.Socket requester = context.createSocket(SocketType.REQ);
            boolean conecto = requester.connect("tcp://localhost:5555");
            if (conecto) {


                menu showMenu = new menu();
                showMenu.main(args);


                // Send requests to server
                String[] requests = (menu.getLastCommand()).split(" ");;
                if((menu.getLastCommand()).startsWith("request")) {
                    for (String request : requests) {
                        //hacer la accion de request
                        System.out.println("Sending request: " + request);
                        requester.send(request.getBytes(ZMQ.CHARSET), 0);

                        // Wait for response from server
                        String reply = requester.recvStr(0).trim();
                        System.out.println("Received reply: " + reply);
                    }
                }
            }
        }
    }
}
