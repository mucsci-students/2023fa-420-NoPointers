package nopointers;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/** Tests public methods from the Puzzle class.
 *
 * @author kstigelman
 */
class PuzzleTest {
    private Puzzle puzzle;

    /** Generates a new puzzle instance on each test with a new set of letters.
     *
     * @author kstigelman
     */
    @BeforeEach
    public void generate () {
        puzzle = new Puzzle();
    }

    /** Tests that valid guesses to the base puzzle are marked correct and invalid guesses are not.
     *
     * @author kstigelman
     */
    @Test
    @DisplayName("Ensure a guess is or is not valid")
    public void testGuess () {
        puzzle = new Puzzle ("pedagogy", 'a');

        assertEquals(puzzle.guessWord("pedagogy"), GuessOutcome.SUCCESS, "Pangram used for base word should be valid.");
        assertEquals(puzzle.guessWord("gap"), GuessOutcome.TOO_SHORT, "Word must be at least 4 letters!");
        assertEquals(puzzle.guessWord("zapped"), GuessOutcome.INCORRECT, "Word can not contain letters not in the puzzle.");
        assertEquals(puzzle.guessWord("pedagogy"), GuessOutcome.ALREADY_FOUND, "Word has already been found.");
        assertEquals(puzzle.guessWord("daddy"), GuessOutcome.SUCCESS, "Word should be valid.");

        char requiredLetter = puzzle.getRequiredLetter();
        String[] guesses = { "gaggy", "dope" };

        if (requiredLetter == 'a' || requiredLetter == 'g' || requiredLetter == 'y')
            assertEquals(puzzle.guessWord("dope"), GuessOutcome.MISSING_REQUIRED);
        else
            assertEquals(puzzle.guessWord("gaggy"), GuessOutcome.MISSING_REQUIRED);
    }

    /** Tests that the order of letters successfully shuffles.
     *
     * @author kstigelman
     */
    @RepeatedTest(10)
    @DisplayName("Ensure letters get shuffled")
    public void testShuffle () {
        char[] beforeShuffle = Arrays.copyOf(puzzle.getLetters(), 7);
        puzzle.shuffleLetters();
        assertFalse (Arrays.equals (beforeShuffle, puzzle.getLetters()), "Letters should be in a new shuffled order");
    }

    /** Tests that the ranking system is correct for no words guessed and also max words guessed.
     *
     * @author kstigelman
     */
    @RepeatedTest(5)
    @DisplayName("Rank should be 0 at 0 points and 9 at max points")
    public void testRank () {
        assertEquals(0, puzzle.getRank(), "Puzzle should not have any points and no guesses");
        for (String s : puzzle.getValidWords())
            puzzle.guessWord(s);

        assertEquals(9, puzzle.getRank(), "Puzzle should have max rank at max points");
    }

    /** Tests that the point percentages for the ranking system are correct.
     *
     * @author kstigelman
     */
    @RepeatedTest(5)
    @DisplayName("Percentage should be 0% at no words guessed and 100% at all words found")
    public void testScorePercent () {
        assertEquals(0.0, puzzle.getScorePercent(), "Puzzle should have 0% and no guesses");
        for (String s : puzzle.getValidWords())
            puzzle.guessWord(s);

        assertEquals(1.0, puzzle.getScorePercent(), "Puzzle should have 100% at all words found");
    }

    /** Tests that the required letter is correct.
     *
     * @author kstigelman
     */
    @RepeatedTest(3)
    @DisplayName("Ensure the required letter is correctly selected.")
    public void testRequiredLetter () {
        assertEquals(puzzle.getLetters()[6], puzzle.getRequiredLetter(), "The required letter should be the last letter in the array.");
        puzzle.shuffleLetters();
        assertEquals(puzzle.getLetters()[6], puzzle.getRequiredLetter(), "The required letter should not change after shuffling");
    }

    @RepeatedTest(5)
    public void testMementoMethods () {
        Puzzle.Memento memento = puzzle.saveToMemento();
        char[] letters = memento.getLetters();
        ArrayList<String> guessed = memento.getGuessed();
        char requiredLetter = memento.getRequiredLetter();
        int score = memento.getScore();

        puzzle = new Puzzle ();
        puzzle.restoreFromMemento(memento);
        assertEquals(letters, puzzle.getLetters(), "Saved memento's letters should match the restored puzzle's letters.");
        assertEquals(guessed, puzzle.getGuessed(), "Saved memento's guessed list should match the restored puzzle's guessed list.");
        assertEquals(requiredLetter, puzzle.getRequiredLetter(), "Saved memento's required letter should match the restored puzzle's required letter.");
        assertEquals(score, puzzle.getScore(), "Saved memento's score should match the restored puzzle's score.");
    }
}