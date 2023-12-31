package nopointers;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;


public class Connect {
    public static String[] numToWords = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
            "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen" };
    static char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    public static String getSqlString(char[] letters) {
        ArrayList<Character> nots = new ArrayList<>();
        ArrayList<Character> characters = new ArrayList<>();

        for (int i = 0; i < letters.length; ++i) {
            nots.add(letters[i]);
        }
        for (int i = 0; i < 26; ++i) {
            if (!nots.contains(alphabet[i]))
                characters.add(alphabet[i]);
        }

        String sql = "SELECT * FROM words WHERE word LIKE '%" + letters[6] + "%'";
        for (int i = 0; i < characters.size(); ++i) {
            sql += " AND word NOT LIKE '%" + characters.get(i) + "%'";
        }
        sql += ";";
        return sql;
    }

    // Get words should be used to
    public static ArrayList<String> getWords(char[] letters) {

        String url = "jdbc:sqlite::resource:words.db";
        ArrayList<String> wordList = new ArrayList<>();
        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);

            /*
            Function.create(conn, "REGEXP", new Function() {
                @Override
                protected void xFunc() throws SQLException {
                    String expression = value_text(0);
                    String value = value_text(1);

                    if (value == null)
                        value = "";

                    Pattern pattern = Pattern.compile(expression);
                    result(pattern.matcher(value).find() ? 1 : 0);
                }
            });*/

            if (conn != null) {
                Statement stmt;

                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(getSqlString(letters));

                while (rs.next()) {
                    wordList.add(rs.getString(1));
                }

                stmt.close();

                conn.close();
            }
        } catch (SQLException e) {
            // Database access error
            return wordList;
        }
        return wordList;
    }

    public static boolean checkPangram(String pangram) {
        String url = "jdbc:sqlite::resource:words.db";
        String sql = "SELECT * FROM pangrams WHERE pangram = '" + pangram + "';";
        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);

            boolean isPangram = false;
            if (conn != null) {
                Statement stmt;

                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(sql);
                isPangram = rs.next();
                return isPangram;
            }
        } catch (SQLException e) {
            // Database access error
            return false;
        }
        return false;
    }

    public static char[] convertToArray (String word) {
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
            arr[i] = (char) lst.get(i);
        }
        return arr;
    }
    public static char[] convertToArray (char[] word) {
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
            arr[i] = (char) lst.get(i);
        }
        return arr;
    }
    public static char[] selectPangram() {
        String url = "jdbc:sqlite::resource:words.db";
        String sql = "SELECT * FROM pangrams ORDER BY RANDOM() LIMIT 1;";

        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);

            String word = "";
            if (conn != null) {
                Statement stmt;

                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next())
                    word = rs.getString(1);

                stmt.close();
                conn.close();
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
                    arr[i] = (char) lst.get(i);
                }
                return arr;
            }
        } catch (SQLException e) {
            // Database access error
            return null;
        }
        return null;
    }
    public static void search(int size, String letters) {
        String url = "jdbc:sqlite:words.db";

        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);

            /*Function.create(conn, "REGEXP", new Function() {
                @Override
                protected void xFunc() throws SQLException {
                    String expression = value_text(0);
                    String value = value_text(1);

                    if (value == null)
                        value = "";

                    Pattern pattern = Pattern.compile(expression);
                    boolean b = pattern.matcher(value).find();

                    result(b ? 1 : 0);
                }
            });*/

            if (conn != null) {
                Statement stmt;

                stmt = conn.createStatement();
                String SQLQuery = letters;

                ResultSet rs = stmt.executeQuery(letters);
                int count = 0;
                while (rs.next()) {
                    System.out.print(rs.getString(1) + ", ");
                    ++count;
                }
                System.out.print(count);
                stmt.close();

                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Database creation error: " + e.getMessage());
        }

    }

    public static String access(String word) {
        // Database URL
        String url = "jdbc:sqlite:words.db";
        int size = word.length();
        String res = "";

        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                Statement smnt = conn.createStatement();
                ResultSet rs = smnt.executeQuery(word);
                res = rs.getString(1);
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Database creation error: " + e.getMessage());
            return "false";
        }
        return res;
    }

    public static boolean findWord(int size, String wordString, Connection conn) {
        if (size < 4) {
            return false;
        }

        Statement stmt;
        try {
            stmt = conn.createStatement();
            String SQLQuery = "SELECT * FROM " + numToWords[size] + "words WHERE word = \"" + wordString + "\";";
            ResultSet rs = stmt.executeQuery(SQLQuery);
            boolean found = rs.next();
            stmt.close();
            return found;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return false;
        }
    }

    /**
     * Counts the number of pangrams in a puzzle
     *
     * @return Number of pangrams
     */
    public static int pangramCount() {
        String url = "jdbc:sqlite::resource:words.db";
        String sql = "SELECT COUNT(*) FROM pangrams;";

        try {
            Connection conn = DriverManager.getConnection(url);
            int res = 0;
            if (conn != null) {
                Statement stmt;

                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next())
                    res = rs.getInt(1);

                stmt.close();
                conn.close();
                return res;
            }
        } catch (SQLException e) {
            System.err.println("Database access error: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Returns the amount of perfect pangrams in
     * a puzzle
     *
     * @return The number of perfect pangrams
     */
    public static int countPerfectPangrams() {
        String url = "jdbc:sqlite::resource:words.db";
        String sql = "SELECT * FROM pangrams;"; // Change the SQL query to select all columns
    
        try {
            Connection conn = DriverManager.getConnection(url);
            int perfectPangramCount = 0;
    
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
    
                while (rs.next()) {
                    String pangramText = rs.getString("pangram");
                    if (isPerfectPangram(pangramText)) {
                        perfectPangramCount++;
                    }
                }
    
                stmt.close();
                conn.close();
    
                return perfectPangramCount;
            }
        } catch (SQLException e) {
            System.err.println("Database access error: " + e.getMessage());
        }
        return 0; // Return 0 for error or no perfect pangrams found
    }

    /**
     * Determines if a text is a perfect pangram
     *
     * @param text The text to be determined
     * @return True if it is a perfect pangram false if not
     */
    public static boolean isPerfectPangram(String text) {
        if(text.length() != 7){
            return false;
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        text = text.toLowerCase().replaceAll(" ", "");

        for (char letter : alphabet.toCharArray()) {
            if (text.indexOf(letter) == -1) {
                return false;
            }
        }
        
        return true;
    }
}
