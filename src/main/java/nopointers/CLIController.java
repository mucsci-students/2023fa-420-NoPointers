package nopointers;


import java.util.Scanner;


//import com.github.cliftonlabs.json_simple.JsonException;

/**
 *
 * @author Christian Michel
 * @
 */
public class CLIController {

    //private Scanner scanner;
    //private Puzzle puzzle;
    private GameStateModel gameStateModel;
    private CLIView view;

    public CLIController() {
        this.gameStateModel = new GameStateModel();
        this.view = new CLIView();

    }



    /**
     *
     * @param command The command from the user that will be parsed.
     * @postcondition The users command is parsed and executed.
     */
    public void handleCommand(String command) {

        switch (command) {
            case "exit":
                view.output("\033[49m");
                System.exit(0);
            case "":
                view.output("Please enter a command");
                break;
            case "show":

                showPuzzle();
                break;
            case "save":

                gameStateModel.savePuzzle();
                break;
            case "guess":

                GuessOutcome outcome = gameStateModel.guess(view.getInput(command));
                handleOutcome(outcome);
                break;
            case "shuffle":

                gameStateModel.shuffle();
                break;
            case "rules":
                rules();
                break;
            case "load":
                //load(getdefaultPath());
                gameStateModel.loadPuzzle();
                break;
            case "new":
                //newPuzzle();
                gameStateModel.newRandomPuzzle();
                break;
            case "help":
                commands();
                break;
            case "rank":

                gameStateModel.rank();
                break;
            case "custom":

                if (!gameStateModel.newUserPuzzle(view.getInput(command))) {
                    view.output("Invalid Pangram!");
                }
                break;
            default:
                view.output(command + ": Unknown Command");
        }
    }

    private void handleOutcome(GuessOutcome outcome) {
        switch (outcome) {
            case SUCCESS -> {
                view.output("Correct! Word added to guessed words.");
                showPuzzle();
            }
            case TOO_SHORT -> {
                view.output("Guess is too short!");
            }
            case EMPTY_INPUT -> {
                view.output("No puzzle to guess on!\nPlease generate a puzzle.");
            }
            case INCORRECT -> {
                view.output("Not a valid word.");
            }
            case ALREADY_FOUND -> {
                view.output("Word already found!");
            }
            case MISSING_REQUIRED -> {
                view.output("Incorrect. Does not use required letter.");
                view.output("The Required letter is [" + gameStateModel.requiredLetter() + "]");
            }
        }
    }









    //
    // Method to be called on from Show Puzzle Command. Prints out the puzzle
    // letters to user.
    public void showPuzzle() {
        if (!gameStateModel.hasPuzzle()) {
            view.output("No puzzle to show please load a puzzle or generate a new puzzle");
            return;
        }
        // Print out the seven letters with the required letter in brackets [].
        for (int i = 0; i < gameStateModel.letters().length; ++i) {
            if (i == 6)
                view.output("[");
            //System.out.println(gameStateModel.letters()[i]);
            view.output( gameStateModel.requiredLetter() + " ");
        }
        view.output("]\n");
        view.output("Guessed Words: " + gameStateModel.guessed().toString());
        view.output("                  .'* *.'\n"
                + "               __/_*_*(_\n"
                + "              / _______ \\\n"
                + "             _\\_)/___\\(_/_ \n"
                + "            / _((\\- -/))_ \\\n"
                + "            \\ \\())(-)(()/ /\n"
                + "             ' \\(((()))/ '\n"
                + "            / ' \\)).))/ ' \\\n"
                + "           / _ \\ - | - /_  \\\n"
                + "          (   ( .;''';. .'  )\n"
                + "          _\\\"__ /    )\\ __\"/_\n"
                + "            \\/  \\   ' /  \\/\n"
                + "             .'  '...' ' )\n"
                + "              / /  |  \\ \\\n"
                + "             / .   .   . \\\n"
                + "            /   .     .   \\\n"
                + "           /   /   |   \\   \\\n"
                + "         .'   /    |    '.  '.\n"
                + "     _.-'    /     |     '-. '-._ \n"
                + " _.-'       |      |       '-.  '-. \n"
                + "(________mrf\\____.| .________)____)");
    }



    public void intro() {
        System.out.println("\033[34;1m");
        System.out.println("\t ▄█     █▄   ▄██████▄     ▄████████ ████████▄        ▄█     █▄   ▄█   ▄███████▄     ▄████████    ▄████████ ████████▄     ▄████████ \n"
                + "\t███     ███ ███    ███   ███    ███ ███   ▀███      ███     ███ ███  ██▀     ▄██   ███    ███   ███    ███ ███   ▀███   ███    ███ \n"
                + "\t███     ███ ███    ███   ███    ███ ███    ███      ███     ███ ███▌       ▄███▀   ███    ███   ███    ███ ███    ███   ███    █▀  \n"
                + "\t███     ███ ███    ███  ▄███▄▄▄▄██▀ ███    ███      ███     ███ ███▌  ▀█▀▄███▀▄▄   ███    ███  ▄███▄▄▄▄██▀ ███    ███   ███        \n"
                + "\t███     ███ ███    ███ ▀▀███▀▀▀▀▀   ███    ███      ███     ███ ███▌   ▄███▀   ▀ ▀███████████ ▀▀███▀▀▀▀▀   ███    ███ ▀███████████ \n"
                + "\t███     ███ ███    ███ ▀███████████ ███    ███      ███     ███ ███  ▄███▀         ███    ███ ▀███████████ ███    ███          ███ \n"
                + "\t███ ▄█▄ ███ ███    ███   ███    ███ ███   ▄███      ███ ▄█▄ ███ ███  ███▄     ▄█   ███    ███   ███    ███ ███   ▄███    ▄█    ███ \n"
                + " \t▀███▀███▀   ▀██████▀    ███    ███ ████████▀        ▀███▀███▀  █▀    ▀████████▀   ███    █▀    ███    ███ ████████▀   ▄████████▀  \n"
                + "\t                         ███    ███                                                             ███    ███                         ");
        System.out.println("\n\t\t\t Welcome \u001b[24m To Word Wizards! To begin playing enter a command below!\n\n\n");
        System.out.println("\t\t\t \u001b[4m New \u001b[24m \254  Creates a New Puzzle\n\n");
        System.out.println("\t\t\t \u001b[4m Save \u001b[24m \254 Saves Your Current Puzzle to Your Home Directory\n\n");
        System.out.println("\t\t\t \u001b[4m Load \u001b[24m \254 Loads a Puzzle From Your Home Directory\n\n");
        System.out.println("\t\t\t \u001b[4m Help \u001b[24m \254 Display Additional Commands \n\n");
        System.out.println("\t\t\t \u001b[4m Exit \u001b[24m \254 Exits the Program\n\n");
        System.out.println("\033[49m");
        System.out.flush();

    }

    public void commands() {
        String padding = "               ";
        System.out.println(padding + "============================================================================");
        System.out.println(padding + " New \u001b[24m \254  Creates a New Puzzle\n");
        System.out.println(padding + " Save \u001b[24m \254 Saves Your current puzzle to your Home Directory\n");
        System.out.println(padding + " Exit \u001b[24m \254  Exits the application\n");
        System.out.println(padding + " Load \u001b[24m \254 Loads a Puzzle From Your Home Directory\n");
        System.out.println(padding + " Rules \u001b[24m \254 Display the rules of the game\n");
        System.out.println(
                padding + " Shuffle \u001b[24m \254 Shuffles your puzzle current puzzle re-arranging the words!\n");
        System.out.println(padding + " Guess \u001b[24m \254 Checks if the word you guessed is a valid word!\n");
        System.out.println(padding + " Show \u001b[24m \254 Displays the puzzle and all found words\n");
        System.out.println(
                padding + " Custom \u001b[24m \254 Create a custom puzzle by entering 6 words and a required letter\n");
        System.out.println(padding + "============================================================================");
        System.out.println(padding + " Rank \u001b[24m \254 Displays your current rank in the puzzle including your points\n");
    }

    private void rules() {
        System.out.println("============================================================================================================");
        System.out.println("Welcome to Word Wizards! The goal of this game is to find all the possible words of a generated pangram \nHere are the rules for the game.\n"
                + "\n"
                + "	- You must use the required word in the pangram at least once in your guess.\n"
                + "\n"
                + "	- Your word guess must be greater than 4 letters.\n"
                + "\n"
                + "	- You can only use the letters in the generated puzzle.\n"
                + "\n"
                + "	- Words longer than the minimum 4 letters will be awarded bonus points among other criteria.\n"
                + "\n"
                + "	- Your guess MUST be a valid word to get points.");
        System.out.println("============================================================================================================");

    }


}
