package DATABASE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Question;

/**
 * Handles database operations for retrieving questions from the quiz system.
 * This class provides methods to fetch questions.
 * 
 */
public class DBFetch {
	
	 /**
     * Retrieves a specific question from the database using its ID.
     * 
     * @param id The unique ID of the question for retrieve
     * @return Question object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */

    public static Question getQuestionById(int id) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";
        
        //query to select question by id
        String query = "SELECT * FROM questions WHERE question_id = ?";
        
        System.out.println("Searching for question ID: " + id);
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Create Question object from result set
                Question question = new Question(
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer"),
                    rs.getString("difficulty_level")
                );
                question.setId(rs.getInt("question_id"));
                System.out.println("Found question ID: "+id);
               
                return question;
            } else {
                System.out.println("No question found with ID: " + id);
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching question: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Retrieves all questions from the database.
     * 
     * @return List of all Question objects in the database
     * @throws SQLException if a database access error occurs
     */
    //all questions
    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";
        
        String query = "SELECT * FROM questions ORDER BY question_id";
        
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Question question = new Question(
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer"),
                    rs.getString("difficulty_level")
                );
                question.setId(rs.getInt("question_id"));
                questions.add(question);
            }
            
            
        } catch (SQLException e) {
            System.out.println("Error fetching the questions: " + e.getMessage());
            e.printStackTrace();
        }
        return questions;
    }
}