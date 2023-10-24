package nopointers;

import java.util.Scanner;

public class CLIView {

    private Scanner scanner;
    private CLIController controller;

    public CLIView() {
        this.controller = new CLIController();
        this.scanner = new Scanner(System.in);
        start(scanner);
        scanner.close();
    }




    /**
     *
     * @param sc A scanner that will take input from system.in
     */
    public void start(Scanner sc) {
        boolean isRun = true;
        controller.intro();
        while (isRun) {
            System.out.print("> ");
            String command = sc.nextLine().toLowerCase().trim();
            parser(command);
            System.out.flush();
        }
    }

    private void parser(String command) {
        String[] args = command.split(" ");
        controller.handleCommand(args[0]);
    }

    // Helper method for controller to get user input for guess and custom puzzle.
    public String getInput(String command) {
        String[] args = command.split(" ");
        String input = (args[1]);
        return input;
    }

    public void output(String input) {
        System.out.println(input);
    }

}
