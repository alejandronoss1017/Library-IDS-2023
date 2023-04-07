package com.client;

import logic.Menu;

import java.util.Scanner;

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

            Scanner scanner = new Scanner(System.in);
            Menu menu = new Menu();
            ZMsg msg = new ZMsg();
            while (conecto) {

                menu.askForCommand();
                menu.setLastCommand(scanner.nextLine());

                // Send requests to server
                String[] requests = (menu.getLastCommand()).split(" ");

                for (String string : requests) {
                    msg.add(string);
                }

                if ((menu.getLastCommand()).startsWith("Request")) {
                    // hacer la accion de request
                    System.out.println("Sending request: " + requests[1]);

                    msg.send(requester);

                    // Wait for response from server
                    String reply = requester.recvStr(0).trim();
                    System.out.println("Received reply: " + reply);
                    msg.clear();
                } else if ((menu.getLastCommand()).startsWith("Return")) {
                    // hacer la accion de request
                    System.out.println("Sending request: " + requests[1]);

                    msg.send(requester);

                    // Wait for response from server
                    String reply = requester.recvStr(0).trim();
                    System.out.println("Received reply: " + reply);
                } else if ((menu.getLastCommand()).startsWith("Renewal")) {
                    // hacer la accion de request
                    System.out.println("Sending request: " + requests[1]);

                    msg.send(requester);

                    // Wait for response from server
                    String reply = requester.recvStr(0).trim();
                    System.out.println("Received reply: " + reply);
                }

            }
            scanner.close();
        }
    }
}
