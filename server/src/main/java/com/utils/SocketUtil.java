package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class SocketUtil {

    private static final Logger logger = LoggerFactory.getLogger(SocketUtil.class);

    /**
     * Connects a socket to a given ip and port, and binds it if bind is true.
     * 
     * Bind is used for providing a service, and connect is used for consuming a
     * service. Usually, a server binds to a well-known address, so that clients can
     * connect to it. A client, on the other hand, connects to a server, so it
     * doesn't need to bind to a well-known address.
     * 
     * @param context ZContext object to create the socket.
     * @param type    SocketType type of socket to create.
     * @param ip      String ip to connect to.
     * @param port    String port to connect to.
     * @param bind    boolean true if socket should bind, false if socket should
     *                connect.
     * @return ZMQ.Socket with a bind or a connection.
     */
    public static ZMQ.Socket connectSocket(ZContext context, SocketType type, String ip, String port,
            boolean bind) {
        ZMQ.Socket socket = null;

        try {
            socket = context.createSocket(type);

            if (bind) {
                socket.bind("tcp://" + ip + ":" + port);
            } else {
                socket.connect("tcp://" + ip + ":" + port);
            }
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }

        return socket;
    }
}