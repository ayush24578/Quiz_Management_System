package tests;

import models.Question;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Model Class Tests - Test Data Models and POJOs
 * 
 * This class contains JUnit tests for verifying the data model classes
 * in the Quiz application. It tests the Question model class
 * including constructors, getters, setters and edge cases.
 * 
 */
public class ModelTests {
	/**
     * Tests the Question model constructor with all parameters
     * Verifies that all properties are correctly initialized
     * Checks question text, options, correct answer and difficulty
     * 
     */
    @Test
    public void testQuestionModel() {
        System.out.println("Testing Question model");
        
        // Test constructor with all parameters
        Question question = new Question(
            "What is the capital of France?",
            "London", "Paris", "Berlin", "Madrid",
            "B", "Beginner"
        );
        
        assertEquals("What is the capital of France?", question.getQuestionText());
        assertEquals("London", question.getOptionA());
        assertEquals("Paris", question.getOptionB());
        assertEquals("Berlin", question.getOptionC());
        assertEquals("Madrid", question.getOptionD());
        assertEquals("B", question.getCorrectOption());
        assertEquals("Beginner", question.getDifficulty());
        
        System.out.println("Constructor test passed!");
    }
    /**
     * Tests all getter and setter methods of the Question model
     * Sets each property individually and verifies retrieval
     * Ensures data integrity for all model properties
     */
    @Test
    public void testQuestionSettersAndGetters() {
        System.out.println("Testing Question setters and getters");
        
        Question question = new Question();
        
        // Set all properties
        question.setId(100);
        question.setQuestionText("Test Question");
        question.setOptionA("Option 1");
        question.setOptionB("Option 2");
        question.setOptionC("Option 3");
        question.setOptionD("Option 4");
        question.setCorrectOption("C");
        question.setDifficulty("Advanced");
        
        // Verify all properties
        assertEquals(100, question.getId());
        assertEquals("Test Question", question.getQuestionText());
        assertEquals("Option 1", question.getOptionA());
        assertEquals("Option 2", question.getOptionB());
        assertEquals("Option 3", question.getOptionC());
        assertEquals("Option 4", question.getOptionD());
        assertEquals("C", question.getCorrectOption());
        assertEquals("Advanced", question.getDifficulty());
        
        System.out.println("Setters and getters test passed!");
    }
    /**
     * Tests different constructor variations of the Question model
     * Tests both default constructor and full parameter constructor
     * Verifies that each constructor works correctly
     */
    @Test
    public void testQuestionConstructorOverload() {
        System.out.println("Testing Question constructor variations");
        
        // Test default constructor
        Question defaultQuestion = new Question();
        assertNotNull(defaultQuestion);
        
        // Test full constructor
        Question fullQuestion = new Question(
            "Full question",
            "A", "B", "C", "D",
            "A", "Intermediate"
        );
        
        assertNotNull(fullQuestion);
        assertEquals("Full question", fullQuestion.getQuestionText());
        
        System.out.println("Constructor overload test passed!");
    }
    /**
     * Tests edge cases and boundary conditions for Question model
     * Includes tests with empty strings and very long strings
     * Ensures the model handles unusual data correctly
     * 
     */
    @Test
    public void testQuestionEdgeCases() {
        System.out.println("Testing Question edge cases");
        
        // Test with empty strings
        Question emptyQuestion = new Question("", "", "", "", "", "", "");
        assertEquals("", emptyQuestion.getQuestionText());
        
        // Test with long strings
        String longText = "A".repeat(1000);
        Question longQuestion = new Question(
            longText, longText, longText, longText, longText,
            "A", "Beginner"
        );
        assertEquals(longText, longQuestion.getQuestionText());
        
        System.out.println("Edge cases test passed!");
    }
}