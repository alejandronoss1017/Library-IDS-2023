package logic;

import java.util.Scanner;

public class menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        while (option != 0) {
            System.out.println("================================================================");
            System.out.println("1. Commands explanation");
            System.out.println("2. Enter command");
            System.out.println("0. End execution");

            System.out.print("Choose an option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("============= Renew book =============");
                    System.out.println("renew + <code of the receipt>");
                    System.out.println(" ");

                    System.out.println("============= Return book =============");
                    System.out.println("return + <code of the receipt>");
                    System.out.println(" ");

                    System.out.println("============= Request book ============");
                    System.out.println("request + <code of the book>");
                    System.out.println(" ");
                    break;

                case 2:
                    System.out.println("Enter the command: ");
                    /*Verification that the command is written in the right way*/
                    boolean continuar=false;//para preguntar otra vez si esta mal el comando escrito
                    while (!continuar) {
                        String command = scanner.nextLine();

                        if (command.startsWith("renew") || command.startsWith("request") || command.startsWith("return")) {
                            //enviar comando a el servirdor
                            continuar=true;
                        } else {
                            System.out.println("Enter one of the follow commands: renew, request, return.");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Thanks for use our system...");

                default:
                    System.out.println("Option no valid");
                    break;
            }
        }
        scanner.close();
    }
}

