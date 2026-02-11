package DATABASE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Handles database connection.
 * This class provides a simple way to test the connection 
 * to the MySQL database used by the Quiz application.
 * 
 */
public class DBConnection {
	/**
     * Main method to test database connection.
     * Connects to the MySQL database and prints success/failure message.
     * 
     * @param args
     */
	public static void main(String[] args) {
		String url ="jdbc:mysql://localhost";
		String username = "root";
		String password = "";
		try(Connection conn = DriverManager.getConnection(url, username, password);
				Statement stm = conn.createStatement();){
			System.out.println("Database connection successful");
		}catch(Exception e) {
			//exception handling for database connection fail
			System.out.println("Database connection failed: "+e.getMessage());
		}
	}
}