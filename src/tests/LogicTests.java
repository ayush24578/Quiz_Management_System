package tests;

import logic.QuizGame;
import models.Question;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Game Logic Tests - Test Quiz Game Logic and Rules
 * 
 * This class contains JUnit tests for verifying the core game logic
 * in the Quiz application. It tests the QuizGame class functionality
 * including scoring, round management and game progression.
 * 
 */
public class LogicTests {
    
    private QuizGame quizGame;
    private List<Question> mockQuestions;
    /**
     * Sets up test environment before each test
     * Creates mock questions and initializes a QuizGame instance
     * Uses reflection to inject mock data for testing
     * 
     * @throws Exception if reflection fails
     */
    @BeforeEach
    public void setUp() throws Exception {
        // Create mock questions for testing
        mockQuestions = new ArrayList<>();
        
        // Add 25 questions (enough for 5 rounds of 5 questions each)
        for (int i = 1; i <= 25; i++) {
            Question q = new Question(
                "Test Question " + i,
                "Option A for Q" + i,
                "Option B for Q" + i,
                "Option C for Q" + i,
                "Option D for Q" + i,
                "A", // All correct answers are A for simplicity
                "Beginner"
            );
            q.setId(i);
            mockQuestions.add(q);
        }
        
        // Create a test QuizGame
        quizGame = new QuizGame("Beginner", "testPlayer", 999);
        
        // Use reflection to inject mock questions
        Field allQuestionsField = QuizGame.class.getDeclaredField("allQuestions");
        allQuestionsField.setAccessible(true);
        allQuestionsField.set(quizGame, mockQuestions);
    }
    /**
     * Tests that QuizGame is properly initialized with correct values
     * Verifies difficulty, username, initial round and score
     */
    @Test
    public void testQuizGameInitialization() {
        System.out.println("Testing QuizGame initialization");
        
        assertEquals("Beginner", quizGame.getDifficulty());
        assertEquals("testPlayer", quizGame.getUsername());
        assertEquals(1, quizGame.getCurrentRound());
        assertEquals(0, quizGame.getScore());
        assertEquals(5, quizGame.getQuestionsPerRound());
        assertEquals(5, quizGame.getTotalRounds());
        assertFalse(quizGame.isGameComplete());
        assertFalse(quizGame.isRoundComplete());
        
        System.out.println("Initial round: " + quizGame.getCurrentRound());
        System.out.println("Initial score: " + quizGame.getScore());
    }
    /**
     * Tests submission of correct answers
     * Verifies that score increments properly
     * Ensures round doesn't complete early
     */
    @Test
    public void testSubmitCorrectAnswer() {
        System.out.println("Testing correct answer submission");
        
        boolean roundComplete = quizGame.submitAnswer("A");
        assertEquals(1, quizGame.getScore());
        assertEquals(1, quizGame.getRoundScore());
        assertFalse(roundComplete, "First answer shouldn't complete the round");
        
        System.out.println("After correct answer - Score: " + quizGame.getScore());
    }
    /**
     * Tests submission of wrong answers
     * Verifies that score doesn't increment for wrong answers
     * Ensures round progression continues
     */
    @Test
    public void testSubmitWrongAnswer() {
        System.out.println("Testing wrong answer submission");
        
        boolean roundComplete = quizGame.submitAnswer("B"); // Wrong answer
        assertEquals(0, quizGame.getScore());
        assertEquals(0, quizGame.getRoundScore());
        assertFalse(roundComplete);
        
        System.out.println("After wrong answer - Score: " + quizGame.getScore());
    }
    /**
     * Tests completion of one full round
     * Answers 5 questions and verifies round completion
     * Checks scoring after round completion
     * 
     * @throws Exception if reflection fails
     */
    @Test
    public void testCompleteOneRound() throws Exception {
        System.out.println("Testing completion of one round");
        
        Field currentQuestionIndexField = QuizGame.class.getDeclaredField("currentQuestionIndex");
        currentQuestionIndexField.setAccessible(true);
        
        //5 questions (complete one round)
        for (int i = 1; i <= 5; i++) {
            boolean roundComplete = quizGame.submitAnswer("A"); // All correct
            
            System.out.println("Question " + i + " - Score: " + quizGame.getScore());
            
            if (i == 5) {
                assertTrue(roundComplete, "Round should be complete after 5 questions");
                assertTrue(quizGame.isRoundComplete(), "isRoundComplete should return true");
            } else {
                assertFalse(roundComplete, "Round should not be complete before 5 questions");
            }
        }
        
        assertEquals(5, quizGame.getScore());
        assertEquals(5, quizGame.getRoundScore());
        assertEquals(1, quizGame.getCurrentRound());
        
        System.out.println("Round 1 completed with score: " + quizGame.getScore());
    }
    /**
     * Tests moving to the next round
     * Completes first round, moves to second round
     * Verifies round score resets but total score remains
     * 
     * @throws Exception if reflection fails
     */
    @Test
    public void testMoveToNextRound() throws Exception {
        System.out.println("Testing moving to next round");
        
        // Complete first round
        for (int i = 0; i < 5; i++) {
            quizGame.submitAnswer("A");
        }
        
        // Move to next round
        quizGame.moveToNextRound();
        
        assertEquals(2, quizGame.getCurrentRound());
        assertEquals(0, quizGame.getRoundScore());
        assertEquals(5, quizGame.getScore());
        
        System.out.println("Moved to round: " + quizGame.getCurrentRound());
        System.out.println("Round score reset to: " + quizGame.getRoundScore());
        System.out.println("Total score remains: " + quizGame.getScore());
    }
    /**
     * Tests complete game progression
     * Answers all 25 questions through 5 rounds
     * Verifies game completion and final scoring
     * 
     * @throws Exception if reflection fails
     */
    @Test
    public void testGameCompletion() throws Exception {
        System.out.println("Testing game completion");
        
        // Answer all 25 questions
        for (int i = 0; i < 25; i++) {
            quizGame.submitAnswer("A");
            
            if (quizGame.isRoundComplete() && i < 24) {
                quizGame.moveToNextRound();
            }
        }
        
        assertTrue(quizGame.isGameComplete(), "Game should be complete after 25 questions");
        assertEquals(25, quizGame.getScore());
        assertEquals(5, quizGame.getCurrentRound());
        
        System.out.println("Game completed!");
        System.out.println("Final score: " + quizGame.getScore());
        System.out.println("Final round: " + quizGame.getCurrentRound());
    }
    /**
     * Tests the hasEnoughQuestions method
     * Verifies that game checks for minimum required questions
     * Tests both sufficient and insufficient question scenarios
     * 
     * @throws Exception if reflection fails
     */
    @Test
    public void testHasEnoughQuestions() throws Exception {
        System.out.println("Testing hasEnoughQuestions method");
        
        assertTrue(quizGame.hasEnoughQuestions(), 
            "Should have enough questions (25 available, need 5 per round)");
        
        // Test with insufficient questions
        List<Question> fewQuestions = new ArrayList<>();
        for (int i = 0; i < 3; i++) { // Only 3 questions
            fewQuestions.add(new Question(
                "Q" + i, "A", "B", "C", "D", "A", "Beginner"
            ));
        }
        
        QuizGame insufficientGame = new QuizGame("Beginner", "testPlayer", 999);
        
        Field allQuestionsField = QuizGame.class.getDeclaredField("allQuestions");
        allQuestionsField.setAccessible(true);
        allQuestionsField.set(insufficientGame, fewQuestions);
        
        assertFalse(insufficientGame.hasEnoughQuestions(),
            "Should not have enough questions (3 available, need 5 per round)");
    }
}