package com.client.logic;

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMsg;
import com.client.model.Connection;

public class Operation {

    public static ZMsg createRequest(final String service, final String isbn, final String campus) {

        if (isbn == null || isbn.isEmpty()) {

            System.out.println("The ISBN is required.");
            return null;

        } else if (campus == null || campus.isEmpty()) {

            System.out.println("The campus is required.");
            return null;

        } else if (service == null || service.isEmpty()) {

            System.out.println("The service is required.");
            return null;

        }

        ZMsg request = new ZMsg();
        request.add(service);
        request.add(isbn);
        request.add(campus);

        return request;

    }

    public static void sendRequest(final ZMsg request) {

        Connection connection = Connection.getInstance();

        // Number of retries
        int retryCount = 0;
        int maxRetries = 3;

        while (retryCount < maxRetries) {

            // Creates the request and response messages
            ZMsg response = new ZMsg();

            try {

                connection.getSocket();

                // If the socket does not receive any response within the set time, it will
                // throw a ZMQException
                request.send(connection.getSocket());

                request.destroy();

                // Waits for the response
                response = ZMsg.recvMsg(connection.getSocket());

                // Prints the response
                System.out.println("Response: " + response.getLast().toString());

                // Exit the loop
                break;

            } catch (ZMQException e) {
                System.out.println("The server is not responding. Retrying...");

                if (e.getErrorCode() == ZMQ.Error.ETERM.getCode()) {
                    // The connection has been terminated, try to change the connection
                    connection.changeConnection();
                    retryCount++;
                } else {
                    // Another type of ZMQ.Error, print the message and exit the loop
                    System.out.println("Error: " + e.getMessage());
                    break;
                }
            } catch (Exception e) {
                // Another type of exception, print the message and exit the loop
                System.out.println("Error: " + e.getMessage());
                break;
            }

        }

        if (retryCount >= maxRetries) {
            System.out.println("Maximum retry count reached. Unable to establish a connection.");
            return;
        }

    }
}
