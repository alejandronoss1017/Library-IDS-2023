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

    public static void sendRequest(ZMsg request) {

        Connection connection = Connection.getInstance(Integer.parseInt(request.getLast().toString()));

        // Number of retries
        int retryCount = 0;
        int maxRetries = 3;

        // Creates a copy of the request, if the connection is changed, the request will
        // be sent again with the same data and the same number of retries
        // just with a different connection.
        ZMsg lastRequest = request.duplicate();

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

                    request = lastRequest.duplicate();

                    if (Integer.parseInt(lastRequest.getLast().toString()) == 1) {
                        request.getLast().reset("2");
                    } else {
                        request.getLast().reset("1");
                    }

                    lastRequest = request.duplicate();

                    retryCount++;
                } else if (e.getErrorCode() == ZMQ.Error.EFSM.getCode()) {
                    // The connection is not established, try to change the connection
                    connection.changeConnection();

                    request = lastRequest.duplicate();

                    if (Integer.parseInt(lastRequest.getLast().toString()) == 1) {
                        request.getLast().reset("2");
                    } else {
                        request.getLast().reset("1");
                    }

                    lastRequest = request.duplicate();

                    retryCount++;
                } else {
                    // Another type of ZMQ.Error, print the message and exit the loop
                    System.out.println("Error: " + e.getMessage());
                    break;
                }
            } catch (NullPointerException e) {
                // This is thrown when the connection is not established, the response is null
                System.out.println("The server is not responding. Retrying...");
                connection.changeConnection();

                request = lastRequest.duplicate();

                if (Integer.parseInt(lastRequest.getLast().toString()) == 1) {
                    request.getLast().reset("2");
                } else {
                    request.getLast().reset("1");
                }

                lastRequest = request.duplicate();

                retryCount++;
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
