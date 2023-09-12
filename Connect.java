import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Connect {
	public boolean acsess(String word) {
        // Database URL (You can specify the path where you want to create the database)
        String url = "jdbc:sqlite:/path/to/your/database.db";
        int size = word.length();
        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("Connected to the database");
                boolean res = findWord(size, word, conn);
                
                conn.close();
                System.out.println("Database and table created successfully");
            }
        } catch (SQLException e) {
            System.err.println("Database creation error: " + e.getMessage());
        }
        return res;
    }
	
	public static boolean findWord(int size, String wordString,Connection conn) {
		if(size < 4) {
			return false;
		}
		String num = String.valueOf(size);
		Statement stmt = conn.createStatement();

        // Create a table (You can define your own schema)
        String SQLQuery = "SELECT * FROM " + num + "words WHERE word = '" 
        		+ wordString +"';";
        stmt.executequery(SQLQuery);
        stmt.close();
		return true;
	}
}
