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

    private State state;
    private boolean isDone = false;

    private Scanner sc = new Scanner(System.in);


    private GameState(GameStateBuilder builder) {
        this.puzzle = new Puzzle();
        database = Database.getInstance();
        this.state = new FreshState(this);
    }


    // Guess method to be called on by controller. Calls on puzzle's guessWord method.
    public GuessOutcome guess (String input) {
        GuessOutcome outcome;
        // If GameState is in CompletedState(all words found) just return PUZZLE_COMPLETED rather than calling function.
        if (isDone == true) {
            return GuessOutcome.PUZZLE_COMPLETED;
        }
        if (input.isBlank() || input.length() < 4) {

            return GuessOutcome.TOO_SHORT;

        }
         return puzzle.guessWord(input);
    }
    // Changes state to/from FreshState and CompletedState.
    public void changeState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
    // Marks puzzle as being completed.
    public void setDone(boolean done) {
        this.isDone = done;
    }

    // Save method for controllers to call on.
    public void savePuzzle () throws IOException {
        save();
    }

    // Load method to be called on by controllers.
    public void loadPuzzle() throws IOException {
        load();
    }
    // Create new puzzle method to be called on by controllers.
    public void newRandomPuzzle() throws InterruptedException {
        newPuzzle();
    }

    // Create new puzzle from user input method to be called on by controllers
    public boolean newUserPuzzle(String input) throws InterruptedException {
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

    public Puzzle.Memento getMemento() {return puzzle.saveToMemento();}

    public void restoreFromMemento(Puzzle.Memento m) {puzzle.restoreFromMemento(m);}

    public char[] getLetters() {
        return puzzle.getLetters();
    }

    public int getRank() { return puzzle.getRank(); }

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
    void newPuzzle() throws InterruptedException {
        System.out.println("Generating New Puzzle...");
        time();
        this.puzzle = new Puzzle();
        System.out.println("\nNew Puzzle Generated!");
    }
    /**
     * Displays a loading animation on our terminal.
     */
    private void time() throws InterruptedException {
        for (int i = 0; i < 100; ++i) {
            TimeUnit.MILLISECONDS.sleep(1);
            System.out.print("\u001b[1000D");
            System.out.flush();
            TimeUnit.MILLISECONDS.sleep(1);
            System.out.print((i + 1) + "%");
            System.out.flush();
        }
    }
    // Helper method to generate new puzzle from user input
    private void newPuzzleBase(String input) throws InterruptedException {
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

    public void save() throws IOException {
        // Save current puzzle to a Memento.
        Puzzle.Memento m = puzzle.saveToMemento();
        String s = new String(m.toGSONObject());
        String home = System.getProperty("user.home");
        Files.write(Paths.get(home).resolve( "puzzle.json"), s.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     *
     * Loads the saved puzzle from a JSON file from the given path.
     */

    public void load() throws IOException {
        String home = System.getProperty("user.home");
        Path path = Paths.get(home).resolve("puzzle.json");

        Gson gson = new Gson();
        String json = new String(Files.readAllBytes(path));
        // Load json to a Memento
        Puzzle.Memento m = gson.fromJson(json, Puzzle.Memento.class);
        puzzle = new Puzzle();
        // Make current puzzle's fields to those of the saved Memento
        puzzle.restoreFromMemento(m);
    }

    /**
     * Called the print func from puzzle
     * and returns the string of the total hints.
     *
     * @return A string of the total hints
     */
    public String hints(){
        String res = puzzle.printHints();
        return res;
    }


    /**
     * Adds the current score into the database of
     * high scores.
     *
     * @param name The name of the user.
     * @return True if it is added and false if it
     * isn't.
     */
    public boolean addScore(String name){
        return puzzle.newHighScore(getScore(),name);
    }

    /**
     * Returns total list of high scores.
     *
     * @return The string of the total
     * list of high scores.
     */
    public String printScore(){
        return puzzle.printScore();
    }


    // Builder implementation for GameState
    public static class GameStateBuilder {
        private Puzzle puzzle;
        private Database database;
        private State state;
        private boolean isDone = false;

        public GameStateBuilder(Database database) {
            this.puzzle = new Puzzle();
            this.database = database;
            this.isDone = false;
            this.state = state;
        }

        public GameState build() {
            return new GameState(this);
        }

    }

}