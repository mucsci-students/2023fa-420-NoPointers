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
import com.google.gson.Gson;
import javafx.stage.FileChooser;

import java.util.concurrent.TimeUnit;


public class GameState {
    // Fields
    private Puzzle puzzle;
    private Database database;

    private GameState(GameStateBuilder builder) {
        this.puzzle = new Puzzle();
        database = Database.getInstance();
    }


    // Guess method to be called on by controller. Calls on puzzle's guessWord method.
    public GuessOutcome guess (String input) {
        if (input.isBlank() || input.length() < 4) {

            return GuessOutcome.TOO_SHORT;

        }
         return puzzle.guessWord(input);
    }

    // Save method for controllers to call on.
    public boolean savePuzzle () {
        if (puzzle != null) {
            //memento.save();
            save();
            System.out.println("Puzzle Saved to Home Directory");
            return true;
        }
        else {
            System.out.println("No Puzzle to Save");
            return false;
        }
    }

    // Load method to be called on by controllers.
    public boolean loadPuzzle() {
        return load();
    }
    // Create new puzzle method to be called on by controllers.
    public void newRandomPuzzle() {
        newPuzzle();
    }

    // Create new puzzle from user input method to be called on by controllers
    public boolean newUserPuzzle(String input) {
        if (input.length() < 7) {
            return false;
        } else if (database.checkPangram(input)) {
            newPuzzleBase(input);
            puzzle.shuffleLetters();
            return true;
        }
        return false;
    }

    public char requiredLetter() {
        return puzzle.getRequiredLetter();
    }

    public ArrayList<String> guessed() {
        return puzzle.getGuessed();
    }

    public boolean hasPuzzle() {
        return (puzzle != null);
    }

    public Puzzle.Memento getMemento() {return puzzle.saveToMemento();}

    public void restoreFromMemento(Puzzle.Memento m) {puzzle.restoreFromMemento(m);}

    public char[] getLetters() {
        return puzzle.getLetters();
    }

    public int getRank() { return puzzle.getRank(); }

    public String[] getRanks() { return puzzle.getRanks(); }

    public int getScore() { return puzzle.getScore(); }

    // Shuffle method
    public void shuffle() {
        puzzle.shuffleLetters();
    }

    // Rank method
    public void rank() {
        puzzle.displayRank();
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
        puzzle = new Puzzle(input);
        System.out.println("\nNew Puzzle Generated!");
    }




    /**
     * @precondtion The user has a puzzle to save in the first place.
     *
     * Saves the users current puzzle to a path if the path is valid.
     *
     * @postcondition The users puzzle is saved to the given path.
     */

    public void save() {

        if(puzzle != null)
        {
            // Save current puzzle to a Memento.
            Puzzle.Memento m = puzzle.saveToMemento();
            String s = new String(m.toGSONObject());
            String home = System.getProperty("user.home");


            System.out.println(s);
            try {
                Files.write(Paths.get(home).resolve("puzzle.json"), s.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }

    }

    /**
     *
     * Loads the saved puzzle from a JSON file from the given path.
     */

    public boolean load() {
        String home = System.getProperty("user.home");
        Path path = Paths.get(home).resolve("puzzle.json");
        try {
            Gson gson = new Gson();
            String json = new String(Files.readAllBytes(path));
            // Load json to a Memento
            Puzzle.Memento m = gson.fromJson(json, Puzzle.Memento.class);
            puzzle = new Puzzle();
            // Make current puzzle's fields to those of the saved Memento
            puzzle.restoreFromMemento(m); 
            return true;
        } catch (IOException err) {
            System.out.println("No Save Found");
            return false;
        }
    }

    // Builder implementation for GameState
    public static class GameStateBuilder {
        private Puzzle puzzle;
        private Database database;

        public GameStateBuilder(Database database) {
            this.puzzle = new Puzzle();
            this.database = database;
        }

        public GameState build() {
            return new GameState(this);
        }

    }


}

