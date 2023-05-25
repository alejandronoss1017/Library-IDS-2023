package com.client.model;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Connection singleton class
 */
public class Connection {

    private static Connection instance;
    private ZContext context;
    private ZMQ.Socket socket;
    private String ipServer;
    private String portServer;
    private String timeout;

    private Connection() {
        // Load the .env file
        Dotenv dotenv = Dotenv.load();

        // Get the IP, PORT and TIMEOUT from the .env file
        this.ipServer = dotenv.get("IP_SERVER_A");
        this.portServer = dotenv.get("PORT_SERVER_A");
        this.timeout = dotenv.get("TIMEOUT");

        // Initialize the context and the socket
        try {
            this.context = new ZContext();
            this.socket = context.createSocket(SocketType.REQ);
            this.socket.connect("tcp://" + ipServer + ":" + portServer);

            // Timeout set to 5 seconds
            this.socket.setReceiveTimeOut(Integer.parseInt(timeout));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static synchronized Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public void changeConnection() {
        // Load the .env file
        Dotenv dotenv = Dotenv.load();

        if (this.ipServer.equals(dotenv.get("IP_SERVER_A"))) {
            // Close and free the resources of the actual context and the actual socket
            socket.close();
            context.close();

            // Obtains the backup IP and PORT from the .env file
            this.ipServer = dotenv.get("IP_SERVER_B");
            this.portServer = dotenv.get("PORT_SERVER_B");
        } else {
            // Close and free the resources of the actual context and the actual socket
            socket.close();
            context.close();

            // Obtains the backup IP and PORT from the .env file
            this.ipServer = dotenv.get("IP_SERVER_A");
            this.portServer = dotenv.get("PORT_SERVER_A");
        }

        // Create a new context and a new socket
        try {
            this.context = new ZContext();
            this.socket = context.createSocket(SocketType.REQ);
            this.socket.connect("tcp://" + ipServer + ":" + portServer);
            this.socket.setReceiveTimeOut(Integer.parseInt(timeout));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ZContext getContext() {
        return context;
    }

    public ZMQ.Socket getSocket() {
        return socket;
    }

    public String getIpServer() {
        return ipServer;
    }

    public String getPortServer() {
        return portServer;
    }

}
