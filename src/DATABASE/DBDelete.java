package DATABASE;

import java.sql.*;

/**
 * Provides database operations for deleting questions from the quiz system.
 * This class handles the deletion of quiz questions based on their question_id.
 * 
 */
public class DBDelete {
	/**
     * Deletes a question from the database using its question_id.
     * 
     * @param questionId The unique ID of the question to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean deleteQuestion(int questionId) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";
        
        //query to delete the question
        String query = "DELETE FROM questions WHERE question_id = ?";
        
        System.out.println("Deleting question ID: " + questionId);
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, questionId);
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("Rows affected by delete: " + rowsAffected);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting the question: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
