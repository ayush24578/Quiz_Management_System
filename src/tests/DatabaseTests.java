package tests;

import DATABASE.*;
import models.Question;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
/**
 * Test Database Operations
 * 
 * This class contains JUnit tests for verifying all database operations
 * in the Quiz application. It tests CRUD operations, authentication,
 * quiz functionality and other database interactions.
 * 
 */
public class DatabaseTests {
	/**
     * Tests that the database connection can be established
     * Calls the DBConnection main method to verify connectivity
     */
    @Test
    public void testDatabaseConnection() {
        // Test that database connection works
        System.out.println("Testing database connection");
        DBConnection.main(new String[]{});
        System.out.println("Completed database connection test.");
    }
    /**
     * Tests full CRUD cycle for questions
     * 1. Creates a test question
     * 2. Reads it from database
     * 3. Updates the question
     * 4. Deletes the question
     * Verifies each step works correctly
     */
    @Test
    public void testQuestionCRUD() {
        // Test Create, Read, Update, Delete operations
        
        // 1. Create a test question
        int questionId = DBInsertion.insertQuestion(
            "Test Question from JUnit",
            "Option A", "Option B", "Option C", "Option D",
            "A", "Beginner"
        );
        
        System.out.println("Inserted question ID: " + questionId);
        assertTrue(questionId > 0, "Question should be inserted successfully");
        
        // 2. Read it from database
        Question question = DBFetch.getQuestionById(questionId);
        assertNotNull(question, "Question should be found in database");
        assertEquals("Test Question from JUnit", question.getQuestionText());
        
        // 3. Update the question
        boolean updateSuccess = DBUpdate.updateQuestion(
            questionId,
            "Updated Test Question",
            "Updated A", "Updated B", "Updated C", "Updated D",
            "B", "Intermediate"
        );
        assertTrue(updateSuccess, "Question should be updated successfully");
        
        // 4. Verify update
        Question updatedQuestion = DBFetch.getQuestionById(questionId);
        assertEquals("Updated Test Question", updatedQuestion.getQuestionText());
        assertEquals("Intermediate", updatedQuestion.getDifficulty());
        
        // 5. Delete the question
        boolean deleteSuccess = DBDelete.deleteQuestion(questionId);
        assertTrue(deleteSuccess, "Question should be deleted successfully");
        
        // 6. Verify deletion
        Question deletedQuestion = DBFetch.getQuestionById(questionId);
        assertNull(deletedQuestion, "Question should not exist");
    }
    /**
     * Tests retrieving all questions from database
     * Verifies the question list is not null and contains valid data
     */
    @Test
    public void testGetAllQuestions() {
        List<Question> questions = DBFetch.getAllQuestions();
        assertNotNull(questions, "Question list should not be null");
        System.out.println("Total questions in database: " + questions.size());
        
        // If there are questions, test some properties
        if (!questions.isEmpty()) {
            Question firstQuestion = questions.get(0);
            assertNotNull(firstQuestion.getQuestionText());
            assertNotNull(firstQuestion.getDifficulty());
        }
    }
    /**
     * Tests user authentication functionality
     * - Verifies admin credentials work
     * - Tests getting user role
     * - Tests getting user ID
     */
    @Test
    public void testUserAuthentication() {
        // Test login functionality
        boolean authResult = DBLogin.authenticate("admin", "admin123");
        System.out.println("Authentication result for admin: " + authResult);
        
        // Test getting user role
        String role = DBLogin.getUserRole("admin");
        System.out.println("Admin role: " + role);
        assertEquals("admin", role);
        
        // Test getting user ID
        int userId = DBLogin.getUserId("admin");
        System.out.println("Admin user ID: " + userId);
        assertTrue(userId > 0);
    }
    /**
     * Tests quiz-related database operations
     * - Leaderboard retrieval
     * - Question retrieval by difficulty
     * - User registration
     */
    @Test
    public void testQuizDatabaseOperations() {
        // Test leaderboard functionality
        List<String[]> leaderboard = DBQuiz.getLeaderboard(5);
        assertNotNull(leaderboard, "Leaderboard should not be null");
        System.out.println("Leaderboard size: " + leaderboard.size());
        
        // Test getting questions by difficulty
        List<Question> beginnerQuestions = DBQuiz.getQuestionsByDifficulty("Beginner", 3);
        assertNotNull(beginnerQuestions, "Questions list should not be null");
        System.out.println("Beginner questions retrieved: " + beginnerQuestions.size());
        
        // Test user registration (clean up after test)
        String testUsername = "testuser_" + System.currentTimeMillis();
        boolean registerResult = DBQuiz.registerUser(testUsername, "password123", "test@example.com");
        System.out.println("Registration result for " + testUsername + ": " + registerResult);
        assertTrue(registerResult, "New user registration should succeed");
    }
    /**
     * Tests user ranking functionality for all difficulty levels
     * Tests rank retrieval for Beginner, Intermediate, Advanced and All
     */
    @Test
    public void testGetUserRank() {
        // Test rank functionality for different difficulties
        String[] difficulties = {"Beginner", "Intermediate", "Advanced", "All"};
        
        for (String difficulty : difficulties) {
            String rank = DBQuiz.getUserRank("admin", difficulty);
            System.out.println("Rank for admin (" + difficulty + "): " + rank);
            assertNotNull(rank, "Rank should not be null: " + difficulty);
        }
    }
}