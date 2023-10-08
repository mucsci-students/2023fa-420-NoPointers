package nopointers;
// Serves as a model for controllers to use.
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import com.github.cliftonlabs.json_simple.JsonArray;
//import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.util.concurrent.TimeUnit;


public class GameState {
    // Fields
    private Puzzle puzzle;


    // Guess method to be called on by controller. Calls on puzzle's guessWord method.
    public GuessOutcome guess (String input) {
        if (puzzle == null) {

            return GuessOutcome.EMPTY_INPUT;

        }
        else if (input.isBlank() || input.length() < 4) {

            return GuessOutcome.TOO_SHORT;

        }

         return puzzle.guessWord(input);


    }

    // Save method for controllers to call on.
    public void savePuzzle () {
        if (puzzle != null) {
            save();
            System.out.println("Puzzle Saved!");

        }
        System.out.println("No Puzzle to Save");
    }

    // Load method to be called on by controllers.
    public void loadPuzzle() {
        load(getdefaultPath());
    }
    // Create new puzzle method to be called on by controllers.
    public void newRandomPuzzle() {
        newPuzzle();
    }

    // Create new puzzle from user input method to be called on by controllers
    public boolean newUserPuzzle(String input) {
        if (input.length() < 2 || input == null || input.length() < 7) {
            return false;

        } else if (Connect.checkPangram(input)) {
            newPuzzleBase(input);
            puzzle.shuffleLetters();
            return true;
        }
        return false;
    }

    public char requiredLetter() {
        return puzzle.getRequiredLetter();
    }

    public char[] letters() {
        return puzzle.getLetters();
    }

    public ArrayList<String> guessed() {
        return puzzle.getGuessed();
    }

    public boolean hasPuzzle() {
        return (puzzle != null);
    }


    public char[] getLetters() {
        return puzzle.getLetters();
    }

    // Shuffle method
    public void shuffle() {
        if (puzzle == null) {
            System.out.println("No puzzle to shuffle!");

        }
        puzzle.shuffleLetters();
    }

    // Rank method
    public void rank() {
        if(puzzle != null)
        {
            puzzle.displayRank();

        }
        System.out.println("No puzzle to rank!");
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
    // Helper method to generate new puzzle from user input
    private void newPuzzleBase(String input) {
        //  Auto-generated method stub
        System.out.println("Generating New Puzzle...");
        time();
        this.puzzle = new Puzzle(input);
        System.out.println("\nNew Puzzle Generated!");
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
}
