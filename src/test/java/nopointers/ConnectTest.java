package nopointers;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/** Tests public methods from the Connect class, which interfaces with the database.
 *
 * @author kstigelman
 */
public class ConnectTest {
    /** Tests retrieving all words from the database that contain the letters of the pangram.
     *
     * @author kstigelman
     */
    @Test
    public void getWordsTest () {
        char[] letters = { 'p', 'a', 'n', 'g', 'r', 'm', 's' };
        ArrayList<String> words = Connect.getWords(letters);
        assertNotNull(words, "The ArrayList should not be null.");
        assertFalse (words.isEmpty(), "The valid words list should not be empty.");

        //Set the letters to an invalid pangram (all letter 'a')
        Arrays.fill(letters, 'a');

        words = Connect.getWords(letters);

        assertNotNull(words, "The ArrayList should not be null.");
        assertTrue (words.isEmpty(), "The valid words list should not be empty.");
    }

    /** Tests reducing a word down to a character array
     *
     * @author kstigelman
     */
    @Test
    public void convertToArrayTest () {

        assertNull(Connect.convertToArray(""));
        assertNull(Connect.convertToArray("".toCharArray()));

        String word = "cars";
        assertNull (Connect.convertToArray(word), "Size less than 7 so convertToArray should fail");
        assertNull (Connect.convertToArray(word.toCharArray()), "Size less than 7 so convertToArray should fail");

        word = "rewatch";
        assertEquals (0, Arrays.compare(Connect.convertToArray(word), Connect.convertToArray(word.toCharArray())), "Both version of convertToArray should result in the same char array");

        word = "nationalization";
        assertEquals (0, Arrays.compare(Connect.convertToArray(word), Connect.convertToArray(word.toCharArray())), "Both version of convertToArray should result in the same char array");

        word = "nationalizations";
        assertNull (Connect.convertToArray(word));
        assertNull (Connect.convertToArray(word.toCharArray()));
    }

    /** Tests selecting a base word pangram from the database and reducing it to a character array of 7 letters.
     *
     * @author kstigelman
     */
    @RepeatedTest(10)
    public void selectPangramTest () {
        char[] pangram = Connect.selectPangram();

        assertNotNull(pangram, "Array of pangram letters is not null");
        assertEquals(7, pangram.length, "Pangram array must have 7 letters");

        HashSet<Character> chars = new HashSet<>();
        for (int i = 0; i < pangram.length; ++i)
            chars.add(pangram[i]);

        assertEquals(7, chars.size(), "Selected pangram has exactly 7 unique letters");
    }
    /** Tests checking to see if words are valid 7 letter pangrams.
     *
     * @author kstigelman
     */
    @Test
    public void checkPangramTest () {
        assertTrue(Connect.checkPangram("pangrams"));
        assertTrue(Connect.checkPangram("ultrastructural"));
        assertFalse(Connect.checkPangram("assemblz"));
        assertFalse(Connect.checkPangram("metaphoric"));
        assertFalse(Connect.checkPangram("apple"));
        assertFalse(Connect.checkPangram(""));
    }
}