package com.utils;

import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class MessageUtil {

    public static void sendMessage(ZMQ.Socket socket, ZMsg msg) {
        msg.send(socket);
    }

    public static void sendMessage(ZMQ.Socket socket, String message) {

        ZMsg msg = new ZMsg();

        msg.add(message);

        msg.send(socket);
    }

}
