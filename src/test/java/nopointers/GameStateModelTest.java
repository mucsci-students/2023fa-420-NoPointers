package nopointers;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/** Tests public methods from the Game State class.
 *
 * @author kstigelman
 */
class GameStateModelTest {
    private GameStateModel gameStateModel;

    /** Generates a new game state puzzle instance on each test with a new set of letters.
     *
     * @author kstigelman
     */
    @BeforeEach
    public void generate () {
        gameStateModel = new GameStateModel();
        gameStateModel.newRandomPuzzle();
    }

    /** Tests that valid guesses to the base puzzle have the correct value.
     *
     * @author kstigelman
     */
    @Test
    @DisplayName("Ensure a guess returns the correct GuessOutcome enum value")
    public void testGuess () {
        gameStateModel = new GameStateModel();
        assertEquals(gameStateModel.guess("t"), GuessOutcome.EMPTY_INPUT, "Puzzle not initialized.");
        gameStateModel.newRandomPuzzle();
        assertEquals(gameStateModel.guess("t"), GuessOutcome.TOO_SHORT, "Guess is too short");

        gameStateModel.newUserPuzzle("pedagogy");
        assertEquals(gameStateModel.guess("pedagogy"), GuessOutcome.SUCCESS, "Pangram used for base word should be valid.");
        assertEquals(gameStateModel.guess("gap"), GuessOutcome.TOO_SHORT, "Word must be at least 4 letters!");
        assertEquals(gameStateModel.guess("zapped"), GuessOutcome.INCORRECT, "Word can not contain letters not in the puzzle.");
        assertEquals(gameStateModel.guess("pedagogy"), GuessOutcome.ALREADY_FOUND, "Word has already been found.");
        assertEquals(gameStateModel.guess("daddy"), GuessOutcome.SUCCESS, "Word should be valid.");

        char requiredLetter = gameStateModel.requiredLetter();
        String[] guesses = { "gaggy", "dope" };

        if (requiredLetter == 'a' || requiredLetter == 'g' || requiredLetter == 'y')
            assertEquals(gameStateModel.guess("dope"), GuessOutcome.MISSING_REQUIRED);
        else
            assertEquals(gameStateModel.guess("gaggy"), GuessOutcome.MISSING_REQUIRED);
    }

    /** Tests that the order of letters successfully shuffles.
     *
     * @author kstigelman
     */
    @RepeatedTest(10)
    @DisplayName("Ensure letters get shuffled")
    public void testShuffle () {
        char[] beforeShuffle = Arrays.copyOf(gameStateModel.getLetters(), 7);
        gameStateModel.shuffle();
        assertFalse (Arrays.equals (beforeShuffle, gameStateModel.getLetters()), "Letters should be in a new shuffled order");
    }
}