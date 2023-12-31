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

    private static final String RESET_TERMINAL_COLOR = "\033[0m";
    private static final String RED_TERMINAL_COLOR = "\u001b[31;1m";
    private static final String PRINT_RED_TERMINAL_COLOR = "\u001b[31;1;3m";
    private GameState gameState;

    private Database database;

    public CLI() throws IOException {
        //gameState = new GameState();
        //gameState = new GameState.GameStateBuilder(Database.getInstance());
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();

        database = Database.getInstance();

        try {
            start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the REPL
     * @throws IOException
     */
    public void start() throws IOException {
        terminal = TerminalBuilder.builder().system(true).build();
        intro();

        try {
            reader = new LineReaderImpl (terminal);
            reader.setCompleter(new AutoCompleter().updateCompleter());
            history = new DefaultHistory(reader);
            history.attach(reader);
        } catch (IOException e) {
            System.out.println("Terminal Error! ");
        }

        while (true) {
            terminal.writer().print(RED_TERMINAL_COLOR +">");
            String command = reader.readLine().toLowerCase();
            parser(command);

        }
    }

    /**
     *
     * Function that parses nad evaluates different cli
     * commands
     *
     * @param command The command from the user that will be parsed.
     * @postcondition The users command is parsed and executed.
     */
    private void parser(String command) {

        String[] args = command.split(" ");
        int i = 0;
        switch (args[0]) {
            case "exit":
                if(database.checkScore(gameState.getScore())){
                    promptWinner();
                    System.out.println(gameState.printScore());
                }
                System.out.println("\033[49m");

                System.out.println(RESET_TERMINAL_COLOR);

                System.exit(0);
            case "":
                System.out.println("Please enter a command");
                break;
            case "show":
                showPuzzle();
                break;
            case "save":
                try {
                    gameState.savePuzzle();
                }
                catch (IOException e) {
                    System.out.print("Saving failed");
                }
                break;
            case "guess":
                if(args.length > 1)
                {
                    GuessOutcome outcome = gameState.guess(args[1].trim());
                    handleOutcome(outcome);
                    break;
                }
                else {
                    terminal.writer().write("Blank Guess!\n");
                    break;
                }
            case "shuffle":

                gameState.shuffle();
                showPuzzle();
                System.out.println("\n");

                terminal.writer().println("Puzzle Shuffled!");
                break;
            case "rules":
                rules();
                break;
            case "load":
                try {
                    gameState.loadPuzzle();
                }
                catch (IOException e) {
                    System.out.println("No Save Found");
                }
                break;
            case "new":
                if (i != 0) {
                    promptWinner();
                    System.out.println(gameState.printScore());
                }
                ++i;
                try {
                    gameState.newRandomPuzzle();
                    showPuzzle();
                }
                catch (InterruptedException e) {
                    System.out.println ("Something went wrong. Please try again.");
                }
                break;
            case "help":
                commands();
                break;
            case "rank":
                gameState.rank();
                break;
            case "custom":
                if(args.length > 1) {
                    try {
                        if (!gameState.newUserPuzzle(args[1])) {
                            System.out.println("Invalid Pangram!");
                        }
                    }
                    catch (InterruptedException e) {
                        System.out.println ("Something went wrong. Please try again.");
                    }
                }
                else {
                    terminal.writer().println("Invalid New Puzzle!");
                }
                break;
            case "hints":
                String res = gameState.hints();
                System.out.print(res);
                break;
            default:
                System.out.println(command + ": Unknown Command");
        }
    }

    /**
     * This function prompt the user
     * to enter their name and it will
     * add it into the database.
     *
     */
    private void promptWinner() {
        System.out.print("Enter name: ");
        String user = reader.readLine().toLowerCase();
        boolean res = gameState.addScore(user);
        if(!res){System.out.print("did not add.");}
        System.out.println(gameState.getScore());
    }

    /**
     * Function that prints different outcome based
     * on what the user entered in as a guess
     *
     * @param outcome The outcome of the guess
     */
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
            case PUZZLE_COMPLETED -> {
                // Uses the onGuess() function from either FreshState or Completed State.
                // When a user successfully enters the last word, FreshState's text will be returned.
                // Subsequent attempts to guess words will cause CompletedState's text to appear.
                System.out.println(gameState.getState().onGuess());
            }
        }
    }









    //
    // Method to be called on from Show Puzzle Command. Prints out the puzzle
    // letters to user.
    public void showPuzzle() {
        if (gameState == null) {
            System.out.println("No puzzle to show please load a puzzle or generate a new puzzle");
            return;
        }
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
        // Print out the seven letters with the required letter in brackets [].
        gameState.rank();
        System.out.println(PRINT_RED_TERMINAL_COLOR);
        for (int i = 0; i < gameState.getLetters().length; ++i) {
            if (i == 6)
                System.out.print("[");
            System.out.print(gameState.getLetters()[i]);
        }
        System.out.print("]\n");
        System.out.println("Guessed Words: " + gameState.guessed().toString());
        System.out.println(RED_TERMINAL_COLOR);
    }


    /**
     * Prints the intro and title of the game
     */
    private void intro() {
        terminal.writer().write(RED_TERMINAL_COLOR);
        String logo = "\n" +
                "/***\n" +
                " *                                                                                                                                                          \n" +
                " *         ##### /    ##   ###                               ##            ##### /    ##   ###                                               ##             \n" +
                " *      ######  /  #####    ###                               ##        ######  /  #####    ###     #                                         ##            \n" +
                " *     /#   /  /     #####   ###                              ##       /#   /  /     #####   ###   ###                                        ##            \n" +
                " *    /    /  ##     # ##      ##                             ##      /    /  ##     # ##      ##   #                                         ##            \n" +
                " *        /  ###     #         ##                             ##          /  ###     #         ##                                             ##            \n" +
                " *       ##   ##     #         ##    /###   ###  /###     ### ##         ##   ##     #         ## ###    ######      /###   ###  /###     ### ##    /###    \n" +
                " *       ##   ##     #         ##   / ###  / ###/ #### / #########       ##   ##     #         ##  ###  /#######    / ###  / ###/ #### / ######### / #### / \n" +
                " *       ##   ##     #         ##  /   ###/   ##   ###/ ##   ####        ##   ##     #         ##   ## /      ##   /   ###/   ##   ###/ ##   #### ##  ###/  \n" +
                " *       ##   ##     #         ## ##    ##    ##        ##    ##         ##   ##     #         ##   ##        /   ##    ##    ##        ##    ## ####       \n" +
                " *       ##   ##     #         ## ##    ##    ##        ##    ##         ##   ##     #         ##   ##       /    ##    ##    ##        ##    ##   ###      \n" +
                " *        ##  ##     #         ## ##    ##    ##        ##    ##          ##  ##     #         ##   ##      ###   ##    ##    ##        ##    ##     ###    \n" +
                " *         ## #      #         /  ##    ##    ##        ##    ##           ## #      #         /    ##       ###  ##    ##    ##        ##    ##       ###  \n" +
                " *          ###      /##      /   ##    ##    ##        ##    /#            ###      /##      /     ##        ### ##    /#    ##        ##    /#  /###  ##  \n" +
                " *           #######/ #######/     ######     ###        ####/               #######/ #######/      ### /      ##  ####/ ##   ###        ####/   / #### /   \n" +
                " *             ####     ####        ####       ###        ###                  ####     ####         ##/       ##   ###   ##   ###        ###       ###/    \n" +
                " *                                                                                                             /                                            \n" +
                " *                                                                                                            /                                             \n" +
                " *                                                                                                           /                                              \n" +
                " *                                                                                                          /                                               \n" +
                " */\n";
        terminal.writer().print(logo);
        terminal.writer().print("\n\t\t\t Welcome \u001b[24m To Word Wizards! To begin playing enter a command below!\n\n\n");
        terminal.writer().print("\t\t\t \u001b[4m New \u001b[24m \254  Creates a New Puzzle\n\n");
        terminal.writer().print("\t\t\t \u001b[4m Save \u001b[24m \254 Saves Your Current Puzzle to Your Home Directory\n\n");
        terminal.writer().print("\t\t\t \u001b[4m Load \u001b[24m \254 Loads a Puzzle From Your Home Directory\n\n");
        terminal.writer().print("\t\t\t \u001b[4m Help \u001b[24m \254 Display Additional Commands \n\n");
        terminal.writer().print("\t\t\t \u001b[4m Exit \u001b[24m \254 Exits the Program\n\n");

    }

    /**
     * Prints a list of the commands and what they do
     */
    public void commands() {
        System.out.println(RED_TERMINAL_COLOR);
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

    /**
     * Prints the rules of the game to the user
     *
     */
    private void rules() {
        System.out.println(RED_TERMINAL_COLOR);
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
