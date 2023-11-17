package nopointers;

import javafx.util.Pair;

import java.sql.*;
import java.util.*;

/** Database class to handle all accesses to the 'words' sqlite database.
 *    Implemented with the Singleton design pattern.
 *
 * @author kstigelman, yohannesgeleta
 */
public class Database {
    private final static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private static Database instance;
    private static Connection connection;

    //private static Connection sconnection;

    /** Constructor establishes connection to the database.
     *
     * @author kstigelman
     */
    private Database () {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite::resource:words.db");
            //sconnection = DriverManager.getConnection("jdbc:sqlite::src/main/resources/words.db");
        }
        catch (SQLException e) {
            connection = null;
            //sconnection = null;
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
            // Create a connection to the database
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(getSqlString(letters));

            while (rs.next()) {
                wordList.add(rs.getString(1));
            }

            stmt.close();
        } catch (SQLException e) {
            // Database access error
            return wordList;
        }
        return wordList;
    }

    /** Checks if the given word is a pangram.
     * @authors kstigelman
     *
     * @param word The given word.
     * @return Whether or not the word exists in the pangram database.
     */
    public boolean checkPangram(String word) {
        String sql = "SELECT * FROM pangrams WHERE pangram = '" + word + "';";
        try {
            // Create a connection to the database
            boolean isPangram = false;
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            isPangram = rs.next();
            stmt.close();
            return isPangram;
        } catch (SQLException e) {
            // Database access error
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

        try {
            String word = "";
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next())
                word = rs.getString(1);

            stmt.close();
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
        } catch (SQLException e) {
            // Database access error
            return null;
        }
    }

    /**
     * This is a java function that checks
     * a score is an acceptable high score.
     *
     * @param score The score of the user.
     * @return True if the score is a new
     * high score and false if it isn't.
     */
    public boolean checkScore(int score) {
        if(scoreCount() < 10){
            return true;
        }
        String sql = "SELECT MIN(scores) FROM highscores;";
        try {
            int res = 0;
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next())
                res = rs.getInt(1);

            stmt.close();
            if(score > res) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            // Database access error
            throw new IllegalArgumentException("checkScore failed: " + e.getMessage());
        }
    }

    /**
     * This is a java function that adds a new
     * highscore to the sql database.
     *
     * @param score The score of the user.
     * @param name The name of the user.
     * @return True if the score is inputted
     * and false if it isn't
     */
    public boolean addScore(int score, String name){
        int id = 0;
        if(scoreCount() > 10){
            id = deleteScore();
        }
        else{
            id = scoreCount() + 1;
        }
        String sql = "INSERT INTO highscores (ID,NAME,SCORE) VALUES ("+ id +", '"+ name +"', "+ score +");";
        try {
            Statement stmt;

            stmt = connection.createStatement();
            int rowsaff = stmt.executeUpdate(sql);


            stmt.close();
            if(rowsaff > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            // Database access error
            throw new IllegalArgumentException("addScore failed: " + e.getMessage());
        }
    }

    /**
     * Deletes the lowest highscore in
     * the database.
     *
     * @return The ID of the deleted score
     */
    public int deleteScore(){
        String sqla = "SELECT ID FROM highscores WHERE scores = (SELECT MIN() FROM highscores)";
        String sqlb = "DELETE FROM highscores WHERE scores = (SELECT MIN(scores) FROM highscores);";
        try {
            Statement stmt;

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqla);
            int rowsaff = stmt.executeUpdate(sqlb);

            if (rowsaff == 0) {
                throw new IllegalStateException("deletion didn't work");
            }
            connection.commit();
            stmt.close();
            return rs.getInt(1);
        } catch (SQLException e) {
            // Database access error
            throw new IllegalArgumentException("deleteError" + e.getMessage());
        }
    }

    /**
     * Return the number of scores in
     * the database.
     *
     * @return The number of scores in
     * the database.
     */
    public int scoreCount(){
        String sql = "SELECT COUNT(*) FROM highscores;";
        try {
            int res = 0;
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next())
                res = rs.getInt(1);

            stmt.close();
            return res;

        } catch (SQLException e) {
            // Database access error
            return -1;
        }
    }

    /**
     * A function that returns a map
     * of type string and int that
     * contain the name and scores of
     * all the values in the database.
     *
     * If there is more the one of the same name in
     * the map then the next of the same name has a
     * dot and num.
     * Ex. John, .1John, .2John
     *
     * @return Map with the names and score
     * of the high scores databse.
     */
    public Map<String, Integer> totalScore(){
        String sql = "SELECT * FROM highscores;";
        try {
            Map<String,Integer> map = new TreeMap<>(Comparator.reverseOrder());
            int num = 0;
            String word = "";
            int i = 1;
            Statement stmt;

            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                word = rs.getString("name");
                num = rs.getInt("score");
                if(map.containsKey(word)){
                    map.put("."+ String.valueOf(i) + word, num);
                    i++;
                }else{
                    map.put(word,num);
                }
            }

            stmt.close();

            return map;

        } catch (SQLException e) {
            // Database access error
            return null;
        }
    }

    /**
     * Closes the connection of the database.
     * Throws an exception if the connection doesn't
     * close.
     */
    public void conClose(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
