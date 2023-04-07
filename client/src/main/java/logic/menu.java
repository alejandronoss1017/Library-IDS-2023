package logic;

public class Menu {
    // variable de instancia para almacenar el último comando ingresado por el
    // usuario
    private String lastCommand = "";

    public void setLastCommand(String command) {

        if (command.startsWith("Renew") || command.startsWith("Request") || command.startsWith("Return")) {
            lastCommand = command; // actualiza el último comando ingresado por el usuario
        } else if (command.equals("help")) {
            showMenu();
        }
    }

    public void askForCommand() {
        System.out.println("Enter the command: ");
    }

    public void showMenu() {
        System.out.println("================================================================");
        System.out.println("============= Renewal book =============");
        System.out.println("Renewal + <code of the receipt>");
        System.out.println(" ");

        System.out.println("============= Return book =============");
        System.out.println("Return + <code of the receipt>");
        System.out.println(" ");

        System.out.println("============= Request book ============");
        System.out.println("Request + <code of the book>");
        System.out.println(" ");
    }

    public String getLastCommand() {
        return lastCommand; // método que devuelve el último comando ingresado por el usuario
    }
}
