package com.server.model;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Publisher {
    ZMQ.Socket pubSocket;
    ZMQ.Socket requestSocket;

    public Publisher(ZContext context) {
        this.pubSocket = context.createSocket(SocketType.PUB);
        this.requestSocket = context.createSocket(SocketType.REQ);
    }

    public ZMQ.Socket getPubSocket() {
        return pubSocket;
    }

    public void setPubSocket(ZMQ.Socket pubSocket) {
        this.pubSocket = pubSocket;
    }

    public ZMQ.Socket getRequestSocket() {
        return requestSocket;
    }

    public void setRequestSocket(ZMQ.Socket requestSocket) {
        this.requestSocket = requestSocket;
    }
}
