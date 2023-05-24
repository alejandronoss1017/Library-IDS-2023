package com.client;

import com.client.logic.Operation;
import com.client.model.Menu;

public class App {
    public static void main(String[] args) {

        String service = "";
        String isbn = "";
        int campus = 0;
        String fileName = "";

        // If there's no arguments print the menu
        if (args.length == 0) {
            Menu.showMenu();
            return;
        }

        boolean hasService = false;
        boolean hasIsbn = false;
        boolean hasCampus = false;

        for (int i = 0; i < args.length; i++) {

            // Checks for '-s' flag
            if (args[i].equals("-s")) {
                if (i + 1 < args.length) {
                    service = args[i + 1];
                    hasService = true;
                    i++; // Jumps to the next argument
                } else {
                    System.out.println("Flag '-s' requires a service value (Borrow, Renewal, or Return).");
                    return;
                }
            }
            // Checks for '--isbn' flag
            else if (args[i].equals("--isbn")) {
                if (i + 1 < args.length) {
                    isbn = args[i + 1];
                    hasIsbn = true;
                    i++; // Jumps to the next argument
                } else {
                    System.out.println("Flag '--isbn' requires an ISBN value.");
                    return;
                }
            }
            // Checks for '-c' flag
            else if (args[i].equals("-c")) {
                if (i + 1 < args.length) {
                    try {
                        campus = Integer.parseInt(args[i + 1]);
                        if (campus != 1 && campus != 2) {
                            System.out.println("The value for '-c' flag should be 1 or 2.");
                            return;
                        }
                        hasCampus = true;
                        i++; // Jumps to the next argument
                    } catch (NumberFormatException e) {
                        System.out.println("The value for '-c' flag must be an Integer.");
                        return;
                    }
                } else {
                    System.out.println("The flag '-c' requires a campus value.");
                    return;
                }
            }
            // Checks for '-f' flag
            else if (args[i].equals("-f")) {
                if (i + 1 < args.length) {
                    fileName = args[i + 1];
                    i++; // Jumps to the next argument
                } else {
                    System.out.println("The flag '-f' requires a file name.");
                    return;
                }
            }
        }

        // Check if at least one of the required flags is present
        if (!hasService || !hasIsbn || !hasCampus) {
            System.out.println("Missing one or more required flags. Please provide values for -s, --isbn, and -c.");
            return;
        }

        // Call the appropriate operation based on the provided arguments
        if (service.equalsIgnoreCase("Borrow")) {
            Operation.sendRequest(Operation.createRequest("Borrow", isbn, Integer.toString(campus)));
        } else if (service.equalsIgnoreCase("Renewal")) {
            Operation.sendRequest(Operation.createRequest("Renewal", isbn, Integer.toString(campus)));
        } else if (service.equalsIgnoreCase("Return")) {
            // Logic

        } else {
            System.out.println("Invalid service value. Please use Borrow, Renewal, or Return.");
        }

        if (!fileName.isEmpty()) {
            // Logic
        }

    }
}
