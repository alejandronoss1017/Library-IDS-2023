package com.client.model;

import java.util.Scanner;

import com.client.logic.Operation;

public class Menu {

    public static Integer option = null;
    public static Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        do {

            System.out.println("\t\tWelcome to IDS library");
            System.out.println("================================================================");
            System.out.println("1. Renewal book");
            System.out.println("2. Return book");
            System.out.println("3. Borrow a book");
            System.out.println("0. Exit");
            System.out.println("================================================================");
            System.out.println("Enter your option: ");

            evaluateOption();
        } while (option != 0);
        return;
    }

    public static void evaluateOption() {
        String isbn = null;
        String campus = null;

        try {
            option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    System.out.println("Enter the ISBN of the book: ");
                    isbn = scanner.nextLine();
                    System.out.println("Enter the campus: ");
                    campus = scanner.nextLine();

                    Operation.sendRequest(Operation.createRequest("Renewal", isbn, campus));

                    pressToContinue();
                    clearConsole();
                    break;
                case 2:
                    System.out.println("Enter the ISBN of the book: ");
                    isbn = scanner.nextLine();
                    System.out.println("Enter the campus: ");
                    campus = scanner.nextLine();

                    Operation.sendRequest(Operation.createRequest("Renewal", isbn, campus));

                    pressToContinue();
                    clearConsole();
                    break;
                case 3:
                    System.out.println("Borrow book");

                    // Logic

                    pressToContinue();
                    clearConsole();
                    break;
                case 0:
                    System.out.println("Exit");

                    // Use return to finish the program
                    return;
                default:
                    System.out.println("The option must be between 0 and 3.");
                    clearConsole();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("The option must be an Integer.");
            pressToContinue();
            clearConsole();
            showMenu();
        }
    }

    public static void clearConsole() {
        // Clears the console in both OS
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                return;
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pressToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        return;
    }
}
