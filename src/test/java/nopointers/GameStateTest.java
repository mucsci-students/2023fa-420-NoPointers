package nopointers;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/** Tests public methods from the Game State class.
 *
 * @author kstigelman
 */
class GameStateTest {
    private GameState gameState;

    /** Generates a new game state puzzle instance on each test with a new set of letters.
     *
     * @author kstigelman
     */
    @BeforeEach
    public void generate () {
        //gameState = new GameState();
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();
        try {
            gameState.newRandomPuzzle();
        }
        catch (InterruptedException e) {
            System.out.println ("Something went wrong. Please try again.");
        }

    }

    /** Tests that valid guesses to the base puzzle have the correct value.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Ensure a guess returns the correct GuessOutcome enum value")
    public void testGuess () {
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();
        assertEquals(gameState.guess("t"), GuessOutcome.TOO_SHORT, "Guess is too short");

        assertEquals(gameState.guess(" "), GuessOutcome.TOO_SHORT, "Guess is blank");

        try {
            assertTrue(gameState.newUserPuzzle("picture"), "Should be a valid pangram");
        }
        catch (InterruptedException e) {
            System.out.println ("Something went wrong. Please try again.");
        }
        assertEquals(gameState.guess("picture"), GuessOutcome.SUCCESS, "Pangram used for base word should be valid.");
        assertEquals(gameState.guess("pit"), GuessOutcome.TOO_SHORT, "Word must be at least 4 letters!");
        assertEquals(gameState.guess("pictures"), GuessOutcome.INCORRECT, "Word can not contain letters not in the puzzle.");
        assertEquals(gameState.guess("picture"), GuessOutcome.ALREADY_FOUND, "Word has already been found.");

        char requiredLetter = gameState.requiredLetter();
        String[] guesses = { "pure", "pint" };

        if (requiredLetter == 'u' || requiredLetter == 'e')
            assertEquals(gameState.guess("pint"), GuessOutcome.MISSING_REQUIRED);
        else
            assertEquals(gameState.guess("pure"), GuessOutcome.MISSING_REQUIRED);
    }

    /** Tests that valid guesses to a random base puzzle are correct.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Ensure a guess from a randomized word returns the correct GuessOutcome enum value")
    public void testRandomGuess () {
        Database database = Database.getInstance();
        ArrayList<String> validWords = database.getWords(gameState.getLetters());

        assertNotEquals(validWords.size(), 0, "There should be guessed words.");

        assertEquals(gameState.guess(validWords.get(0)), GuessOutcome.SUCCESS, "Word should be a valid word.");
        assertEquals(gameState.guess(String.valueOf(gameState.getLetters()[0])), GuessOutcome.TOO_SHORT, "Word should be a valid word.");

        Puzzle.Memento memento = gameState.getMemento();
        assertEquals(memento.getGuessed(), gameState.guessed(), "The game state's guessed words and the memento's guessed words should be the same.");
        assertEquals(memento.getScore(), gameState.getScore(), "The game state's score should match the memento's score.");
        gameState.rank();
        assertEquals(memento.getRank(), gameState.getRank(), "The game state's score should match the memento's score.");
        gameState.rank();
    }

    @RepeatedTest(2)
    @DisplayName("Test save functonality")
    public void testSave () {
        try {
            gameState.savePuzzle();
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }
    @RepeatedTest(2)
    @DisplayName("Test load functonality")
    public void testLoad () {
        try {
            gameState.savePuzzle();
            gameState.loadPuzzle();
        }
        catch (IOException e) {
            fail("IOException was thrown");
        }
    }
    @RepeatedTest(2)
    @DisplayName("Test hints")
    public void testHints () {
        try {
            gameState.newUserPuzzle("variety");
            assertNotNull(gameState.hints(), "Hints should be equal.");
        }
        catch (InterruptedException e) {
            fail ("Interrupt exception thrown");
        }
    }

    /** Tests that the order of letters successfully shuffles.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Ensure letters get shuffled")
    public void testShuffle () {
        char[] beforeShuffle = Arrays.copyOf(gameState.getLetters(), 7);
        gameState.shuffle();
        assertFalse (Arrays.equals (beforeShuffle, gameState.getLetters()), "Letters should be in a new shuffled order.");

        Puzzle.Memento memento = gameState.getMemento();
        assertEquals(memento.getLetters(), gameState.getLetters(), "The game state's letters and the memento's letters should be the same.");
    }

    /** Tests that GameState correctly generates a puzzle from a user input.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Ensure a puzzle can be built from user input.")
    public void testNewUserPuzzle () {
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();
        try {
            assertFalse(gameState.newUserPuzzle("t"), "Input is too short");
            assertFalse(gameState.newUserPuzzle("alphabetical"), "Input is not a pangram");
            assertFalse(gameState.newUserPuzzle("assortment"), "Input is not a pangram");

            assertTrue(gameState.newUserPuzzle("abdomen"), "Input is a pangram");
            assertTrue(gameState.newUserPuzzle("variety"), "Input is a pangram");
        }
        catch (InterruptedException e) {
            System.out.println ("Something went wrong. Please try again.");
            fail ("Interrupted. Please try again");
        }
    }

    /** Tests that the required letter is correct.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Ensure the required letter is correctly selected.")
    public void testRequiredLetter () {
        assertEquals(gameState.getLetters()[6], gameState.requiredLetter(), "The required letter should be the last letter in the array.");
        gameState.shuffle();
        assertEquals(gameState.getLetters()[6], gameState.requiredLetter(), "The required letter should not change after shuffling.");

        Puzzle.Memento memento = gameState.getMemento();
        assertEquals(memento.getRequiredLetter(), gameState.requiredLetter(), "The game state's required letter and the memento's required letter should be the same.");
    }

    /** Tests the game state's memento gson object.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Ensure memento's methods work properly.")
    public void testMemento () {
        Puzzle.Memento memento = gameState.getMemento();
        assertNotNull(memento.toGSONObject(), "GSON should not be null.");
        assertFalse(memento.toGSONObject().isBlank(), "GSON should not be blank.");

        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        GameState gameState2 = builder.build();

        gameState2.restoreFromMemento(memento);
        assertEquals(gameState.getRank(), gameState2.getRank(), "Restored memento's score should be the same as old memento.");

        assertEquals(gameState.getScore(), gameState2.getScore(), "Restored memento's score should be the same as old memento.");
        assertEquals(gameState.getLetters(), gameState2.getLetters(), "Restored memento's letters should be the same as old memento.");
        assertEquals(gameState.requiredLetter(), gameState2.requiredLetter(), "Restored memento's required letter should be the same as old memento.");

    }

    /** Tests the game state's score adding and saving functionality.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Test GameState's Scores")
    public void testScores () {
        String prev = gameState.printScore();
        gameState.addScore("Tom Cruise");
        assertTrue(gameState.newScore());
        assertNotEquals(gameState.printScore(), prev, "Scores should not be the same.");
        //gameState.conClose();
    }

    /** Tests the game state's State design pattern functionality.
     *
     * @author kstigelman
     */
    @RepeatedTest(2)
    @DisplayName("Test GameState's State Design Pattern")
    public void testState () {
        assertNotNull(gameState.getState(), "Should not be null.");
        assertTrue(gameState.getState() instanceof FreshState, "Should be a brand new state.");
        gameState.changeState(new CompletedState(gameState));
        assertSame(gameState.guess("banana"), GuessOutcome.PUZZLE_COMPLETED, "Puzzle should be completed.");
    }


}