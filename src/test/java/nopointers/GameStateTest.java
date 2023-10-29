package nopointers;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


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
        gameState = new GameState();
        gameState.newRandomPuzzle();
    }

    /** Tests that valid guesses to the base puzzle have the correct value.
     *
     * @author kstigelman
     */
    @Test
    @DisplayName("Ensure a guess returns the correct GuessOutcome enum value")
    public void testGuess () {
        gameState = new GameState();
        assertEquals(gameState.guess("t"), GuessOutcome.EMPTY_INPUT, "Puzzle not initialized.");
        gameState.newRandomPuzzle();
        assertEquals(gameState.guess("t"), GuessOutcome.TOO_SHORT, "Guess is too short");

        assertEquals(gameState.guess(""), GuessOutcome.TOO_SHORT, "Guess is blank");

        gameState.newUserPuzzle("pedagogy");
        assertEquals(gameState.guess("pedagogy"), GuessOutcome.SUCCESS, "Pangram used for base word should be valid.");
        assertEquals(gameState.guess("gap"), GuessOutcome.TOO_SHORT, "Word must be at least 4 letters!");
        assertEquals(gameState.guess("zapped"), GuessOutcome.INCORRECT, "Word can not contain letters not in the puzzle.");
        assertEquals(gameState.guess("pedagogy"), GuessOutcome.ALREADY_FOUND, "Word has already been found.");
        assertEquals(gameState.guess("daddy"), GuessOutcome.SUCCESS, "Word should be valid.");

        char requiredLetter = gameState.requiredLetter();
        String[] guesses = { "gaggy", "dope" };

        if (requiredLetter == 'a' || requiredLetter == 'g' || requiredLetter == 'y')
            assertEquals(gameState.guess("dope"), GuessOutcome.MISSING_REQUIRED);
        else
            assertEquals(gameState.guess("gaggy"), GuessOutcome.MISSING_REQUIRED);
    }

    /** Tests that the order of letters successfully shuffles.
     *
     * @author kstigelman
     */
    @RepeatedTest(5)
    @DisplayName("Ensure letters get shuffled")
    public void testShuffle () {
        char[] beforeShuffle = Arrays.copyOf(gameState.getLetters(), 7);
        gameState.shuffle();
        assertFalse (Arrays.equals (beforeShuffle, gameState.getLetters()), "Letters should be in a new shuffled order");
    }

    /** Tests that GameState correctly generates a random puzzle.
     *
     * @author kstigelman
     */
    /*
    @RepeatedTest(3)
    public void testNewRandomPuzzle () {
        gameState = new GameState();
        gameState.newRandomPuzzle();
        assertTrue (gameState.hasPuzzle(), "When generating a new random puzzle, the game state should have a non-null puzzle.");
    }

     */

    /** Tests that GameState correctly generates a puzzle from a user input.
     *
     * @author kstigelman
     */
    @Test
    public void testNewUserPuzzle () {
        assertFalse (gameState.newUserPuzzle("t"), "Input is too short");
        assertFalse (gameState.newUserPuzzle("trickery"), "Input is not a pangram");
        assertFalse (gameState.newUserPuzzle("assortment"), "Input is not a pangram");

        assertTrue (gameState.newUserPuzzle("lawyers"), "Input is a pangram");
        assertTrue (gameState.newUserPuzzle("programs"), "Input is a pangram");
    }


    /** Tests that the required letter is correct.
     *
     * @author kstigelman
     */
    /*
    @RepeatedTest(3)
    public void testRequiredLetter () {
        assertEquals(gameState.getLetters()[6], gameState.requiredLetter(), "The required letter should be the last letter in the array.");
        gameState.shuffle();
        assertEquals(gameState.getLetters()[6], gameState.requiredLetter(), "The required letter should not change after shuffling");
    }
     */
}