import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Connect {
	public static String[] numToWords = {
			"zero",
			"one",
			"two",
			"three",
			"four",
			"five",
			"six",
			"seven",
			"eight",
			"nine",
			"ten",
			"eleven",
			"twelve",
			"thirteen",
			"fourteen",
			"fifteen"
	};
	
	public boolean access(String word) {
		// Database URL
		String url = "jdbc:sqlite:src/words";
		int size = word.length();

		boolean res = false;
		try {
		    // Create a connection to the database
		    Connection conn = DriverManager.getConnection(url);
		    if (conn != null) {
			System.out.println("Connected to the database");
			res = findWord(size, word, conn);
			
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
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			 String SQLQuery = "SELECT * FROM " + numToWords[size] + "words WHERE word = \"" 
		        		+ wordString +"\";";
		        ResultSet rs = stmt.executeQuery (SQLQuery);
		        boolean found = rs.next ();
		        stmt.close();
		        return found;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
