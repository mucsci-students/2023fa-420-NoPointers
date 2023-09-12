import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Connect {
	public static void main(String[] args) {
        // Database URL (You can specify the path where you want to create the database)
        String url = "jdbc:sqlite:/path/to/your/database.db";
        Scanner console = new Scanner(System.in);
        String wordString = console.next();
        int size = wordString.length();
        
        try {
            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url);

            if (conn != null) {
                System.out.println("Connected to the database");
                findWord(size, wordString, conn);
                conn.close();
                
                
            }
        } catch (SQLException e) {
		System.out.println("Acsess not granted. Didn't work");
        }
        
    }
	
	public static boolean findWord(int size, String wordString,Connection conn) {
		if(size < 4) {
			return false;
		}
		String num = Integer.toString(size);
		Statement stmt = conn.createStatement();

        // SQL Query to find word in table of specific word size
        String SQLQuery = "SELECT * FROM " + num + "words WHERE word = \"" 
        		+ wordString +"\";";
        stmt.execute(SQLQuery);
        stmt.close();
		return true;
	}
}
