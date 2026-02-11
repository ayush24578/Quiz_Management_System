package logic;

import java.util.List;
import models.Question;
import DATABASE.DBQuiz;

/**
 * Manages the core game logic for Quiz Mania.
 * Handles game state, scoring, round management and question flow.
 * This class coordinates between the UI and database layers.
 * 
 */
public class QuizGame {

    private String difficulty;
    private String username;
    private int userId;
    private boolean gameSaved = false;

    // Game state
    private List<Question> allQuestions;
    private int currentRound = 1;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int roundScore = 0;

    // Game configuration
    private static final int TOTAL_ROUNDS = 5;
    private static final int QUESTIONS_PER_ROUND = 5;
    private static final int TOTAL_QUESTIONS = TOTAL_ROUNDS * QUESTIONS_PER_ROUND;

    /**
     * Creates a new QuizGame instance.
     * 
     * @param difficulty The difficulty level for the game
     * @param username The player's username
     * @param userId The player's user ID
     */
    public QuizGame(String difficulty, String username, int userId) {
        this.difficulty = difficulty;
        this.username = username;
        this.userId = userId;
        loadQuestions();
    }
    /**
     * Loads questions from the database based on difficulty.
     */
    private void loadQuestions() {
        // Load all questions the game (5 rounds × 5 questions = 25 questions)
        allQuestions = DBQuiz.getQuestionsByDifficulty(difficulty, TOTAL_QUESTIONS);

        if (allQuestions.size() < TOTAL_QUESTIONS) {
            System.out.println("Warning: Only " + allQuestions.size() +
                    " questions available for " + difficulty +
                    " difficulty (need " + TOTAL_QUESTIONS + ")");
        }
    }
    /**
     * Gets the current question being displayed.
     * 
     * @return Current Question object or null if game is over
     */
    public Question getCurrentQuestion() {
        if (currentQuestionIndex < allQuestions.size()) {
            return allQuestions.get(currentQuestionIndex);
        }
        return null;
    }
    /**
     * Submits an answer for the current question.
     * 
     * @param selectedOption The option selected by player (A, B, C or D)
     * @return true if round is complete, false otherwise
     */
    public boolean submitAnswer(String selectedOption) {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion == null) {
            return true; // No more questions, end game
        }

        boolean isCorrect = selectedOption.equalsIgnoreCase(currentQuestion.getCorrectOption());

        if (isCorrect) {
            score++;
            roundScore++;
        }

        // Move to next question
        currentQuestionIndex++;

        // Check if round is complete
        if (isRoundComplete()) {
            return true;
        }
        if (isGameComplete()) {
            return true;
        }
        return false;
    }
    /**
     * Check if round is complete
     * 
     * @return true if round is complete, false otherwise
     */
    public boolean isRoundComplete() {
        // Round is complete when we've answered all questions for this round
        // but haven't reached the total limit
        int questionsInCurrentRound = currentQuestionIndex % QUESTIONS_PER_ROUND;
        return (questionsInCurrentRound == 0 && currentQuestionIndex > 0) &&
                !isGameComplete();
    }
    /**
     * Check if game is complete
     * 
     * @return true if game is complete, false otherwise
     */
    public boolean isGameComplete() {
        // Game is complete when:
        // 1. We've answered all available questions
        // 2. We've answered the maximum number of questions (25)
        // 3. We've completed all rounds
        return currentQuestionIndex >= Math.min(allQuestions.size(), TOTAL_QUESTIONS) ||
                currentRound > TOTAL_ROUNDS;
    }
    /**
     * Moves the game to the next round.
     * Resets round score and increments round counter.
     */
    public void moveToNextRound() {
        if (currentRound < TOTAL_ROUNDS) {
            currentRound++;
            roundScore = 0;
        } else {
            // Game is complete
            saveGame();
        }
    }
    /**
     * Checks if the game has been saved to database.
     * 
     * @return true if game is saved, false otherwise
     */
    public boolean isGameSaved() {
        return gameSaved;
    }
    /**
     * Saves the game progress and score to database.
     * Called when game ends or user quits.
     */
    public void saveGame() {
        if (gameSaved) {
            System.out.println("Game already saved, skipping...");
            return;
        }

        // Always save the game when it ends or user quits
        int questionsAnswered = getQuestionsAnswered();
        if (questionsAnswered > 0) {
            boolean saved = DBQuiz.saveScore(userId, difficulty, currentRound, score, questionsAnswered);
            if (saved) {
                gameSaved = true; // Mark as saved
                System.out.println("Game saved: " + saved +
                        ", User: " + userId +
                        ", Score: " + score + "/" + questionsAnswered);
            }
        }
    }

    /**
     * Gets the current round number.
     * 
     * @return Current round number (1-5)
     */
    public int getCurrentRound() {
        return Math.min(currentRound, TOTAL_ROUNDS);
    }
    /**
     * Gets the total number of rounds in the game.
     * 
     * @return Total rounds (5)
     */
    public int getTotalRounds() {
        return TOTAL_ROUNDS;
    }

    /**
     * Gets the number of questions per round.
     * 
     * @return Questions per round (5)
     */
    public int getQuestionsPerRound() {
        return QUESTIONS_PER_ROUND;
    }

    /**
     * Gets the player's total score.
     * 
     * @return Total correct answers
     */
    public int getScore() {
        return score;
    }
    /**
     * Gets the player's score for the current round.
     * 
     * @return Current round score
     */
    public int getRoundScore() {
        return roundScore;
    }
    /**
     * Gets the number of questions answered so far.
     * 
     * @return Count of answered questions
     */
    public int getQuestionsAnswered() {
        return currentQuestionIndex;
    }
    /**
     * Gets the total number of questions available.
     * 
     * @return Total questions count
     */
    public int getTotalQuestions() {
        return Math.min(allQuestions.size(), TOTAL_QUESTIONS);
    }
    /**
     * Gets the game difficulty level.
     * 
     * @return Difficulty level (Beginner/Intermediate/Advanced)
     */
    public String getDifficulty() {
        return difficulty;
    }
    /**
     * Gets the player's username.
     * 
     * @return Username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Gets the current question number within the round.
     * 
     * @return Question number (1-5)
     */
    public int getQuestionNumberInRound() {
        return (currentQuestionIndex % QUESTIONS_PER_ROUND) + 1;
    }
    /**
     * Checks if there are enough questions to start the game.
     * 
     * @return true if minimum questions available, false otherwise
     */
    public boolean hasEnoughQuestions() {
        return allQuestions.size() >= QUESTIONS_PER_ROUND;
    }
    /**
     * Prints game state for debugging purposes.
     * Shows current round, score, and game progress.
     */
    public void printGameState() {
        System.out.println("=== Game State ===");
        System.out.println("Round: " + currentRound + "/" + TOTAL_ROUNDS);
        System.out.println("Question Index: " + currentQuestionIndex);
        System.out.println("Questions Available: " + allQuestions.size());
        System.out.println("Score: " + score);
        System.out.println("Round Score: " + roundScore);
        System.out.println("Questions in Round: " + getQuestionNumberInRound());
        System.out.println("Round Complete: " + isRoundComplete());
        System.out.println("Game Complete: " + isGameComplete());
        System.out.println("==================");
    }
}