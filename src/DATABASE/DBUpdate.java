package DATABASE;


import java.sql.*;

/**
 * Handles database operations for updating existing questions in the Quiz database.
 * This class provides methods to modify question details including text, options,
 * correct answer and difficulty level.
 * 
 */
public class DBUpdate {
	/**
     * Updates an existing question in the database.
     * 
     * @param questionId The ID of the question to update
     * @param question The new question text
     * @param optA The new option A text
     * @param optB The new option B text
     * @param optC The new option C text
     * @param optD The new option D text
     * @param correctOpt The new correct option (A, B, C or D)
     * @param difficulty The new difficulty level (Beginner, Intermediate, Advanced)
     * @return true if update successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean updateQuestion(int questionId, String question, String optA, 
                                         String optB, String optC, String optD, 
                                         String correctOpt, String difficulty) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";
        //query to update question
        String query = "UPDATE questions SET " +
                      "question_text = ?, " +
                      "option_a = ?, " +
                      "option_b = ?, " +
                      "option_c = ?, " +
                      "option_d = ?, " +
                      "correct_answer = ?, " +
                      "difficulty_level = ? " +
                      "WHERE question_id = ?";
        
        System.out.println("Updating question ID: " + questionId);
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            //all parameters set for prepared statement
            pstmt.setString(1, question);
            pstmt.setString(2, optA);
            pstmt.setString(3, optB);
            pstmt.setString(4, optC);
            pstmt.setString(5, optD);
            pstmt.setString(6, correctOpt);
            pstmt.setString(7, difficulty);
            pstmt.setInt(8, questionId); 
            //execute and return number of rows affected
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("Rows affected by update: " + rowsAffected);
            //return true if at least one row was affected
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating question: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
