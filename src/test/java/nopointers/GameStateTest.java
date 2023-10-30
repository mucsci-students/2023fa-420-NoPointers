package nopointers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;


import java.util.ArrayList;
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
        //gameState = new GameState();
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();
        gameState.newRandomPuzzle();
    }

    /** Tests that valid guesses to the base puzzle have the correct value.
     *
     * @author kstigelman
     */
    @RepeatedTest(3)
    @DisplayName("Ensure a guess returns the correct GuessOutcome enum value")
    public void testGuess () {
        //gameState = new GameState();
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();
        assertEquals(gameState.guess("t"), GuessOutcome.EMPTY_INPUT, "Puzzle not initialized.");
        gameState.newRandomPuzzle();
        assertEquals(gameState.guess("t"), GuessOutcome.TOO_SHORT, "Guess is too short");

        assertEquals(gameState.guess(" "), GuessOutcome.TOO_SHORT, "Guess is blank");

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

    /** Tests that valid guesses to a random base puzzle are correct.
     *
     * @author kstigelman
     */
    @RepeatedTest(10)
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
    }

    /** Tests that the order of letters successfully shuffles.
     *
     * @author kstigelman
     */
    @RepeatedTest(5)
    @DisplayName("Ensure letters get shuffled")
    public void testShuffle () {
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();
        assertFalse (gameState.shuffle(), "Puzzle should be null. Shuffle should fail.");
        gameState.newRandomPuzzle();
        char[] beforeShuffle = Arrays.copyOf(gameState.getLetters(), 7);
        assertTrue (gameState.shuffle(), "New Puzzle should be created. Shuffle should succeed.");
        assertFalse (Arrays.equals (beforeShuffle, gameState.getLetters()), "Letters should be in a new shuffled order.");

        Puzzle.Memento memento = gameState.getMemento();
        assertEquals(memento.getLetters(), gameState.getLetters(), "The game state's letters and the memento's letters should be the same.");
    }

    /** Tests that GameState correctly generates a random puzzle.
     *
     * @author kstigelman
     */
    @RepeatedTest(3)
    @DisplayName("Ensure a new random puzzle can be built.")
    public void testNewRandomPuzzle () {
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        gameState = builder.build();
        gameState.newRandomPuzzle();
        assertTrue (gameState.hasPuzzle(), "When generating a new random puzzle, the game state should have a non-null puzzle.");
    }

    /** Tests that GameState correctly generates a puzzle from a user input.
     *
     * @author kstigelman
     */
    @RepeatedTest(3)
    @DisplayName("Ensure a puzzle can be built from user input.")
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
    @RepeatedTest(5)
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
    @RepeatedTest(10)
    @DisplayName("Ensure memento's gson method produces a string")
    public void testMementoGSON () {
        Puzzle.Memento memento = gameState.getMemento();
        assertNotNull(memento.toGSONObject(), "GSON should not be null.");
        assertFalse(memento.toGSONObject().isBlank(), "GSON should not be blank.");
    }
}