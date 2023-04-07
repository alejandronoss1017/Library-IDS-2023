package logic;

import java.util.Scanner;

public class menu {
    private static String lastCommand = ""; // variable de instancia para almacenar el último comando ingresado por el usuario

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


                    /*Verification that the command is written in the right way*/
                    boolean continuar=false;//para preguntar otra vez si esta mal el comando escrito
                    while (!continuar) {
                        System.out.println("Enter the command: ");

                        String command = scanner.nextLine();

                        if (command.startsWith("renew") || command.startsWith("request") || command.startsWith("return")) {
                            lastCommand = command; // actualiza el último comando ingresado por el usuario
                            continuar=true;
                        }

                        if (command.equals("help")){
                            System.out.println("================================================================");
                            System.out.println("============= Renew book =============");
                            System.out.println("renew + <code of the receipt>");
                            System.out.println(" ");

                            System.out.println("============= Return book =============");
                            System.out.println("return + <code of the receipt>");
                            System.out.println(" ");

                            System.out.println("============= Request book ============");
                            System.out.println("request + <code of the book>");
                            System.out.println(" ");
                        }
                    }

        scanner.close();

    }

    public static String getLastCommand() {
        return lastCommand; // método que devuelve el último comando ingresado por el usuario
    }
}
