package nopointers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/** Tests public methods from the Connect class, which interfaces with the database.
 *
 * @author kstigelman
 */
public class DatabaseTest {
    private Database database;


    @BeforeEach
    public void beforeEach () {
        database = Database.getInstance();

        assertNotNull(database, "The database instance should not be null");
    }
    /** Tests retrieving all words from the database that contain the letters of the pangram.
     *
     * @author kstigelman
     */
    @Test
    public void getWordsTest () {
            char[] letters = { 'p', 'a', 'n', 'g', 'r', 'm', 's' };
            ArrayList<String> words = database.getWords(letters);

            assertNotNull(words, "The ArrayList should not be null.");
            assertFalse (words.isEmpty(), "The valid words list should not be empty.");

            //Set the letters to an invalid pangram (all letter 'a')
            Arrays.fill(letters, 'a');

            words = database.getWords(letters);

            assertNotNull(words, "The ArrayList should not be null.");
            assertTrue (words.isEmpty(), "The valid words list should not be empty.");
    }

    /** Tests reducing a word down to a character array
     *
     * @author kstigelman
     */
    @Test
    public void convertToArrayTest () {
        assertNull(database.convertToArray(""));
        assertNull(database.convertToArray("".toCharArray()));

        String word = "cars";
        assertNull (database.convertToArray(word), "Size less than 7 so convertToArray should fail");
        assertNull (database.convertToArray(word.toCharArray()), "Size less than 7 so convertToArray should fail");

        word = "rewatch";
        assertEquals (0, Arrays.compare(database.convertToArray(word), database.convertToArray(word.toCharArray())), "Both version of convertToArray should result in the same char array");

        word = "nationalization";
        assertEquals (0, Arrays.compare(database.convertToArray(word), database.convertToArray(word.toCharArray())), "Both version of convertToArray should result in the same char array");

        word = "nationalizations";
        assertNull (database.convertToArray(word));
        assertNull (database.convertToArray(word.toCharArray()));
    }

    /** Tests selecting a base word pangram from the database and reducing it to a character array of 7 letters.
     *
     * @author kstigelman
     */
    @RepeatedTest(10)
    public void selectPangramTest () {

            char[] pangram = database.selectPangram();

            assertNotNull(pangram, "Array of pangram letters is not null");
            assertEquals(7, pangram.length, "Pangram array must have 7 letters");

            HashSet<Character> chars = new HashSet<>();

            for (char c : pangram)
                chars.add(c);

            assertEquals(7, chars.size(), "Selected pangram has exactly 7 unique letters");

    }
    /** Tests checking to see if words are valid 7 letter pangrams.
     *
     * @author kstigelman
     */
    @Test
    public void checkPangramTest () {
            assertTrue(database.checkPangram("pangrams"));
            assertTrue(database.checkPangram("ultrastructural"));
            assertFalse(database.checkPangram("assemblz"));
            assertFalse(database.checkPangram("metaphoric"));
            assertFalse(database.checkPangram("apple"));
            assertFalse(database.checkPangram(""));
    }
}