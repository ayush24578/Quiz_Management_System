package DATABASE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles user authentication and role management for the quiz system.
 * This class provides methods that verify user credentials and retrieve user information.
 * 
 */

public class DBLogin {
	/**
     * Authenticates a user by checking username and password.
     * 
     * @param username The username to authenticate
     * @param password The password to verify
     * @return true if authentication successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
	
    public static boolean authenticate(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";
        
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if user exists
            
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
            return false;
        }
    }
    
    // Get user role
    /**
     * Retrieves the role of a specific user.
     * 
     * @param username The username to get role for
     * @return The user's role (admin, player, etc.) or null if user not found
     * @throws SQLException if a database access error occurs
     */
    
    public static String getUserRole(String username) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String password = "";
        
        String query = "SELECT role FROM users WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            }
            return null;
            
        } catch (SQLException e) {
            System.out.println("Error getting user role: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Retrieves the user ID for a specific username.
     * 
     * @param username The username to get ID for
     * @return The user's ID or -1 if user not found
     * @throws SQLException if a database access error occurs
     */
    
    public static int getUserId(String username) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String password = "";
        
        String query = "SELECT id FROM users WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
            
        } catch (SQLException e) {
            System.out.println("Error getting user ID: " + e.getMessage());
            return -1;
        }
    }
}