package com.client.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOperation {

    public static List<String> readFile(String fileName) {

        List<String> data = new ArrayList<String>();

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                data.add(line);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        }

        return data;
    }

    public static void sendMessages(List<String> messages) {

        for (String message : messages) {
            String[] data = message.split(" ");
            Operation.sendRequest(Operation.createRequest(data[0], data[1], data[2]));
        }
    }
}
