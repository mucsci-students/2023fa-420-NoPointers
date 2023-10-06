package nopointers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;


import com.github.cliftonlabs.json_simple.JsonArray;
//import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;




import java.util.concurrent.TimeUnit;
/**
 *
 * @author Christian Michel
 * @
 */
public class CLI {

    private Scanner sc;
    private Puzzle puzzle;

    public CLI() {
        Scanner sc = new Scanner(System.in);
        System.out.flush();
        start(sc);

    }


    public void start(Scanner sc) {
        boolean isRun = true;
        intro();
        System.out.flush();

        while (isRun) {
            System.out.print("> ");
            String command = sc.nextLine().toLowerCase().trim();
            parser(command);
            System.out.flush();
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
                System.out.println("\033[49m");
                System.exit(0);
            case "":
                System.out.println("Please enter a command");
                break;
            case "show":
                if (puzzle != null) {
                    puzzle.showPuzzle();
                    break;
                }
                System.out.println("No puzzle to show please load a puzzle or generate a new puzzle");
                break;
            case "save":
                if (puzzle != null) {
                    save();
                    System.out.println("Puzzle Saved!");
                    break;
                }
                System.out.println("No Puzzle to Save");
                break;
            case "guess":
                if (puzzle == null) {
                    System.out.println("No puzzle to guess on!\nPlease generate a puzzle.");
                    break;
                }
                if (args[1].isBlank() || args[1].length() < 4) {
                    System.out.println("Guess is too short!");
                    break;
                }
                puzzle.guessWord(args[1]);
                break;
            case "shuffle":
                if (puzzle == null) {
                    System.out.println("No puzzle to shuffle!");
                    break;
                }
                puzzle.shuffleLetters();
                break;
            case "rules":
                rules();
                break;
            case "load":
                load(getdefaultPath());
                break;
            case "new":
                newPuzzle();
                break;
            case "help":
                commands();
                break;
            case "rank":
                if(puzzle != null)
                {
                    puzzle.displayRank();
                    break;
                }
                System.out.println("No puzzle to rank!");
                break;
            case "custom":
                if (args.length < 2 || args[1] == null || args[1].length() < 7) {
                    System.out.println("Invalid Pangram!");
                    break;
                }
                if (Connect.checkPangram(args[1])) {
                    newPuzzleBase(args[1]);
                    puzzle.shuffleLetters();
                    break;
                }
                break;
            default:
                System.out.println(command + ": Unknown Command");
        }
    }

    /**
     * Displays a loading animation on our terminal.
     */
    void time() {
        for (int i = 0; i < 100; ++i) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                System.out.print("\u001b[1000D");
                System.out.flush();
                TimeUnit.MILLISECONDS.sleep(1);
                System.out.print((i + 1) + "%");
                System.out.flush();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * A default path if the user doesn't pass a custom path.
     *
     * @return The default path to user.home.
     */
    private Path getdefaultPath() {
        String home = System.getProperty("user.home");
        return Paths.get(home).resolve("puzzle.json");
    }
    /**
     * @precondtion If the user has no puzzle generated a new puzzle is generated.
     *              If the user has already generated a puzzle a new puzzle
     *              overwrites the current one.
     *
     *              A command that invokes the new puzzle constroctor and sets it as
     *              our current puzzle.
     *
     * @poscondition We have generated a new puzzle for the user to solve.
     */
    void newPuzzle() {
        System.out.println("Generating New Puzzle...");
        time();
        this.puzzle = new Puzzle();
        System.out.println("\nNew Puzzle Generated!");
    }

    /**
     * @precondtion The user has a puzzle currently generated. Saves the users
     *              "puzzle" as a jSON file in the users home directory.
     * @postcondition The users puzzle is saved in the home directory.
     */
    void save() {
        save(getdefaultPath());
    }

    /**
     * @precondtion The user has a puzzle to save in the first place.
     *
     * @param path The path we want to save the puzzle to.
     *
     *             Saves the users current puzzle to a path if the path is valid.
     *
     * @postcondition The users puzzle is saved to the given path.
     */
    private void save(Path path) {

        JsonArray ja = new JsonArray();
        ja.add(puzzle.toJsonObject());
        String jsonText = ja.toJson();
        try {
            Files.write(path, jsonText.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    /**
     *
     * @param path Loads the saved puzzle from a JSON file from the given path.
     */
    private void load(Path path) {
        String jsonText = null;
        JsonArray ja = null;

        try {
            jsonText = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ja = (JsonArray) Jsoner.deserialize(jsonText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Object object : ja) {
            JsonObject jo = (JsonObject) object;
            Puzzle newPuzzle = Puzzle.fromJsonObject(jo);
            puzzle = newPuzzle;
        }

    }
    private void newPuzzleBase(String input) {
        // TODO Auto-generated method stub
        System.out.println("Generating New Puzzle...");
        time();
        this.puzzle = new Puzzle(input);
        System.out.println("\nNew Puzzle Generated!");
    }

    //

    private void intro() {
        System.out.println("\033[31;1m");
        System.out.println("\n" +
                " ▄█     █▄   ▄██████▄     ▄████████ ████████▄       ▄█     █▄   ▄█   ▄███████▄     ▄████████    ▄████████ ████████▄  \n" +
                "███     ███ ███    ███   ███    ███ ███   ▀███     ███     ███ ███  ██▀     ▄██   ███    ███   ███    ███ ███   ▀███ \n" +
                "███     ███ ███    ███   ███    ███ ███    ███     ███     ███ ███▌       ▄███▀   ███    ███   ███    ███ ███    ███ \n" +
                "███     ███ ███    ███  ▄███▄▄▄▄██▀ ███    ███     ███     ███ ███▌  ▀█▀▄███▀▄▄   ███    ███  ▄███▄▄▄▄██▀ ███    ███ \n" +
                "███     ███ ███    ███ ▀▀███▀▀▀▀▀   ███    ███     ███     ███ ███▌   ▄███▀   ▀ ▀███████████ ▀▀███▀▀▀▀▀   ███    ███ \n" +
                "███     ███ ███    ███ ▀███████████ ███    ███     ███     ███ ███  ▄███▀         ███    ███ ▀███████████ ███    ███ \n" +
                "███ ▄█▄ ███ ███    ███   ███    ███ ███   ▄███     ███ ▄█▄ ███ ███  ███▄     ▄█   ███    ███   ███    ███ ███   ▄███ \n" +
                " ▀███▀███▀   ▀██████▀    ███    ███ ████████▀       ▀███▀███▀  █▀    ▀████████▀   ███    █▀    ███    ███ ████████▀  \n" +
                "                         ███    ███                                                            ███    ███            \n");
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
