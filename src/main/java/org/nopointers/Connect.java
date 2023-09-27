package org.nopointers;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            System.err.println("Database creation error: " + e.getMessage());
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
            System.err.println("Database access error: " + e.getMessage());
        }
        return false;
    }

    public static char[] convertToArray (String word) {
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
            System.err.println("Database access error: " + e.getMessage());
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

    public static boolean access(String word) {
        // Database URL
        String url = "jdbc:sqlite:words.db";
        int size = word.length();

        boolean res = false;
        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                res = findWord(size, word, conn);

                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Database creation error: " + e.getMessage());
            return false;
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
}
