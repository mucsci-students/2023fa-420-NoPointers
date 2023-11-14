package nopointers;


import org.jline.reader.*;
import org.jline.reader.impl.history.*;
import org.jline.reader.impl.completer.*;
import org.jline.reader.impl.*;
import org.jline.terminal.*;
import java.io.IOException;



/**
 *
 * @author Christian Michel
 * @
 */
public class CLI {
    private LineReaderImpl reader;
    private Terminal terminal;
    private ParsedLine parser;
    private History history;
    private Highlighter highlighter;
    //private Puzzle puzzle;
    private GameState gameState;



    public CLI(Terminal t) {
        //gameState = new GameState();
        //gameState = new GameState.GameStateBuilder(Database.getInstance());
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();

        try {
            start(t);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the REPL
     * @throws IOException
     */
    public void start(Terminal t) throws IOException {
        intro();

        try {
            terminal = t;
            reader = new LineReaderImpl (t);
            reader.setCompleter(new AutoCompleter().updateCompleter());
            history = new DefaultHistory(reader);
            history.attach(reader);
        } catch (IOException e) {
            System.out.println("Terminal Error! ");
        }

        while (true) {
            System.out.print(">");
            String command = reader.readLine().toLowerCase().trim();
            parser(command);
        }
    }

    /**
     *
     * @param command The command from the user that will be parsed.
     * @postcondition The users command is parsed and executed.
     */
    private void parser(String command) {
        String[] args = command.split(" ");
        switch (args[0]) {
            case "exit":
                if(gameState.newScore()){
                    promptWinner();
                    System.out.println(gameState.printScore());
                }
                gameState.conClose();
                System.out.println("\033[49m");
                System.exit(0);
            case "":
                System.out.println("Please enter a command");
                break;
            case "show":
                showPuzzle();
                break;
            case "save":
                gameState.savePuzzle();
                break;
            case "guess":
                GuessOutcome outcome = gameState.guess(args[1]);
                handleOutcome(outcome);
                break;
            case "shuffle":

                gameState.shuffle();
                break;
            case "rules":
                rules();
                break;
            case "load":
                gameState.loadPuzzle();
                break;
            case "new":
                gameState.newRandomPuzzle();
                showPuzzle();
                break;
            case "help":
                commands();
                break;
            case "rank":

                gameState.rank();
                break;
            case "custom":

                if (!gameState.newUserPuzzle(args[1])) {
                    System.out.println("Invalid Pangram!");
                }
                break;
            case "hints":
                String res = gameState.hints();
                System.out.print(res);
                break;
            case "test":
                tester();
                break;
            default:
                System.out.println(command + ": Unknown Command");
        }
    }

    private void promptWinner() {

        System.out.print("Enter name: ");
        String user = reader.readLine().toLowerCase();
        boolean res = gameState.addScore(user);
        if(!res){System.out.print("did not add.");}
        System.out.println(gameState.test("SELECT score FROM highscores WHERE name = '"+ user +"';"));
    }

    private void tester() {
        System.out.print("Enter query: ");
        String user = reader.readLine().toLowerCase();
        System.out.println(gameState.test(user));
    }
    private void handleOutcome(GuessOutcome outcome) {
        switch (outcome) {
            case SUCCESS -> {
                System.out.println("Correct! Word added to guessed words.");
                showPuzzle();
            }
            case TOO_SHORT -> {
                System.out.println("Guess is too short!");
            }
            case EMPTY_INPUT -> {
                System.out.println("No puzzle to guess on!\nPlease generate a puzzle.");
            }
            case INCORRECT -> {
                System.out.println("Not a valid word.");
            }
            case ALREADY_FOUND -> {
                System.out.println("Word already found!");
            }
            case MISSING_REQUIRED -> {
                System.out.println("Incorrect. Does not use required letter.");
                System.out.println("The Required letter is [" + gameState.requiredLetter() + "]");
            }
        }
    }









    //
    // Method to be called on from Show Puzzle Command. Prints out the puzzle
    // letters to user.
    public void showPuzzle() {
        if (!gameState.hasPuzzle()) {
            System.out.println("No puzzle to show please load a puzzle or generate a new puzzle");
            return;
        }
        // Print out the seven letters with the required letter in brackets [].
        for (int i = 0; i < gameState.getLetters().length; ++i) {
            if (i == 6)
                System.out.print("[");
            System.out.print(gameState.getLetters()[i]);
        }
        System.out.print("]\n");
        System.out.println("Guessed Words: " + gameState.guessed().toString());
        System.out.println("                  .'* *.'\n"
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



    private void intro() {
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
