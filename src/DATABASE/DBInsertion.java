package DATABASE;

import java.sql.*;

/**
 * Handles database operations for inserting new questions into the quiz system database.
 * This class provides methods to add new quiz questions to the database.
 * 
 */

public class DBInsertion {
	/**
     * Inserts a new question into the database and returns the generated ID.
     * 
     * @param question The question text
     * @param optA Option A text
     * @param optB Option B text
     * @param optC Option C text
     * @param optD Option D text
     * @param correctOpt The correct option (A, B, C, or D)
     * @param difficulty The difficulty level (Beginner, Intermediate, Advanced)
     * @return The generated question ID if successful, -1 otherwise
     * @throws SQLException if a database access error occurs
     */
	
    public static int insertQuestion(String question, String optA, String optB, 
                                     String optC, String optD, String correctOpt, 
                                     String difficulty) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";
        
        //query to insert new question
        String query = "INSERT INTO questions (question_text, option_a, option_b, " +
                      "option_c, option_d, correct_answer, difficulty_level) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, question);
            pstmt.setString(2, optA);
            pstmt.setString(3, optB);
            pstmt.setString(4, optC);
            pstmt.setString(5, optD);
            pstmt.setString(6, correctOpt);
            pstmt.setString(7, difficulty);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                //generated question id
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Inserted question with ID: " + generatedId);
                    return generatedId;
                }
            }
            System.out.println("No rows affected or no generated keys");
            return -1;
            
        } catch (SQLException e) {
            System.out.println("Error inserting the question to database: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}