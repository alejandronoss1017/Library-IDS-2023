package com.server.logic;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static com.server.ServerInfo.*;

public class ActorRenewal implements Runnable {
    private boolean status = false;

    @Override
    public void run() {
        try ( ZContext contextSUB = new ZContext()){
            ZMQ.Socket subscriberSocket = contextSUB.createSocket(SocketType.SUB);

            subscriberSocket.connect(ADDRESSPUBSUB);
            subscriberSocket.subscribe(RENEWALTOPIC.getBytes(ZMQ.CHARSET));
            subscriberSocket.subscribe(CONFIRMATION.getBytes(ZMQ.CHARSET));

            while (!Thread.currentThread().isInterrupted()) {
                subscriberSocket.recvStr();
                if (!status) {
                    if (subscriberSocket.recvStr().equals(CONFIRMATION)) {
                        status = true;
                        subscriberSocket.unsubscribe(CONFIRMATION.getBytes(ZMQ.CHARSET));
                    }
                } else {
                    String message = subscriberSocket.recvStr();
                    System.out.printf("[SUSCRIBER " + RENEWALTOPIC + " " + Thread.currentThread() + "] Received message '%s'%n", message);
                }
            }
            contextSUB.destroy();
        } catch (Exception e) {
            if(e.getMessage().equals("Errno 4")){
                System.out.println("[" + RENEWALTOPIC + " " + Thread.currentThread() + "]: Hilo interrumpido.");
                status = false;
            }
            else
                System.err.println("[" + RENEWALTOPIC + " " + Thread.currentThread() + "]: " + e.getMessage());
        }
    }

    public boolean isStatus() {
        return status;
    }
}
