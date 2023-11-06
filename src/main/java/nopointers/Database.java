package nopointers;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

/** Database class to handle all accesses to the 'words' sqlite database.
 *    Implemented with the Singleton design pattern.
 *
 * @author kstigelman, yohannesgeleta
 */
public class Database {
    private final static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private static Database instance;
    private static Connection connection;

    /** Constructor establishes connection to the database.
     *
     * @author kstigelman
     */
    private Database () {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite::resource:words.db");
        }
        catch (SQLException e) {
            connection = null;
        }
    }

    /** The public instance creation method. Ensures the database class has only 1 instance for the entire project.
     * @author kstigelman
     */
    public synchronized static Database getInstance() {
        if (instance == null)
                instance = new Database();
        return instance;
    }
    /** Builds a SQL string to fetch all words in the database that are valid of the given set of letters.
     * @author kstigelman
     *
     * @param letters The puzzle's valid letters.
     * @return A string to be used for a SQL query.
     */
    private static String getSqlString(char[] letters) {
        ArrayList<Character> nots = new ArrayList<>();
        ArrayList<Character> characters = new ArrayList<>();

        for (int i = 0; i < letters.length; ++i) {
            nots.add(letters[i]);
        }
        for (int i = 0; i < 26; ++i) {
            if (!nots.contains(alphabet.charAt(i)))
                characters.add(alphabet.charAt(i));
        }

        String sql = "SELECT * FROM words WHERE word LIKE '%" + letters[6] + "%'";
        for (int i = 0; i < characters.size(); ++i) {
            sql += " AND word NOT LIKE '%" + characters.get(i) + "%'";
        }
        sql += ";";
        return sql;
    }

    /** Retrieves all words from the database that contains the given set of letters.
     * @author kstigelman
     *
     * @param letters The given set of letters from a puzzle.
     * @return All words in the database containing the letters.
     */
    public ArrayList<String> getWords(char[] letters) {
        ArrayList<String> wordList = new ArrayList<>();

        try {
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(getSqlString(letters));

            while (rs.next()) {
                wordList.add(rs.getString(1));
            }

            stmt.close();
            return wordList;
        }
        catch (SQLException e) {
            return wordList;
        }
    }

    /** Checks if the given word is a pangram.
     * @authors kstigelman
     *
     * @param word The given word.
     * @return Whether or not the word exists in the pangram database.
     */
    public boolean checkPangram(String word) {
        String sql = "SELECT * FROM pangrams WHERE pangram = '" + word + "';";

        boolean isPangram;

        try {
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            isPangram = rs.next();
            stmt.close();
            return isPangram;
        }
        catch (SQLException e) {
            return false;
        }
    }

    /** Converts a word string to a character array. The word must have 7 unique letters to succeed.
     * @author kstigelman
     *
     * @param word The given string
     * @return The word broken up into a 7 letter char array
     */
    public char[] convertToArray (String word) {
        if (word.isBlank() || word.length() != 7)
            return null;
        HashSet<Character> s = new HashSet<>();
        for(char c: word.toCharArray())
        {
            s.add(c);
        }
        ArrayList<Character> lst = new ArrayList<Character>();
        lst.addAll(s);
        char[] arr = new char[7];
        for(int i = 0; i < lst.size(); ++i)
        {
            arr[i] = lst.get(i);
        }
        return arr;
    }
    /** Converts a character array to another character array of size 7. The original array must have 7 unique letters
     *    to succeed.
     * @author kstigelman
     *
     * @param word The given array
     * @return The array reduced to 7 unique letters.
     */
    public char[] convertToArray (char[] word) {
        if (word.length != 7)
            return null;
        HashSet<Character> s = new HashSet<>();
        for(char c : word)
        {
            s.add(c);
        }
        ArrayList<Character> lst = new ArrayList<Character>();
        lst.addAll(s);
        char[] arr = new char[7];
        for(int i = 0; i < lst.size(); ++i)
        {
            arr[i] = lst.get(i);
        }
        return arr;
    }

    /** Select a random pangram from the list of all pangrams
     *
     * @author yohannesgeleta, kstigelman
     *
     * @return a character array of size 7 of the selected pangram's letters.
     */
    public char[] selectPangram() {
        String sql = "SELECT * FROM pangrams ORDER BY RANDOM() LIMIT 1;";

        String word = "";

        try {
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            word = rs.getString(1);

            stmt.close();
            HashSet<Character> s = new HashSet<>();
            for (char c : word.toCharArray()) {
                s.add(c);
            }
            ArrayList<Character> lst = new ArrayList<Character>();
            lst.addAll(s);
            char[] arr = new char[7];
            for (int i = 0; i < lst.size(); ++i) {
                arr[i] = lst.get(i);
            }
            return arr;
        }
        catch (SQLException e) {
            return null;
        }
    }

    public static boolean findWord(int size, String wordString) {
        if (size < 4)
            return false;

        try {
            Statement stmt;

            stmt = connection.createStatement();
            String SQLQuery = "SELECT * FROM words WHERE word = " + wordString + ";";
            ResultSet rs = stmt.executeQuery(SQLQuery);
            boolean found = rs.next();
            stmt.close();
            return found;
        }
        catch (SQLException e) {
            return false;
        }

    }
    public static int pangramCount()  {
        String sql = "SELECT COUNT(*) FROM pangrams;";

        int res = 0;

        try {
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next())
                res = rs.getInt(1);

            stmt.close();
            return res;
        }
        catch (SQLException e) {
            return 0;
        }
    }

    public static int countPerfectPangrams() {

        String sql = "SELECT * FROM pangrams;"; // Change the SQL query to select all columns

        int perfectPangramCount = 0;

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String pangramText = rs.getString("pangram");
                if (isPerfectPangram(pangramText)) {
                    perfectPangramCount++;
                }
            }

            stmt.close();
        }
        catch (SQLException e) {
            return 0;
        }

        return perfectPangramCount;
    }


    public static boolean isPerfectPangram(String text) {

        text = text.toLowerCase().replaceAll(" ", "");

        for (char letter : alphabet.toCharArray()) {
            if (text.indexOf(letter) == -1) {
                return false;
            }
        }
        return true;
    }

}
