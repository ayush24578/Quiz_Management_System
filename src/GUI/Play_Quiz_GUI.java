package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import logic.QuizGame;
import DATABASE.DBQuiz;

/**
 * Main game interface for playing quiz in Quiz.
 * Provides the interactive quiz environment where players can answer questions,
 * track scores and progress through rounds. Handles game flow, scoring,
 * and user interaction for the quiz gameplay experience.
 * 
 */
public class Play_Quiz_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ButtonGroup optionGroup;
	
	// Game components
	private QuizGame quizGame;
	private String username;
	private JLabel difficultyLabel;
	private JLabel roundLabel;
	private JLabel scoreLabel;
	private JLabel questionNumberLabel;
	private JTextArea questionTextArea;
	private JRadioButton optionA;
	private JRadioButton optionB;
	private JRadioButton optionC;
	private JRadioButton optionD;
	private JButton nextButton;

	/**
     * Creates a new PlayQuiz window with specified difficulty and user.
     * Initializes the game logic and user interface for quiz gameplay.
     * 
     * @param difficulty The difficulty level (Beginner, Intermediate, Advanced)
     * @param username The username of the player
     */
	public Play_Quiz_GUI(String difficulty, String username) {
		this.username = username;
		int userId = DBQuiz.getUserId(username);
		this.quizGame = new QuizGame(difficulty, username, userId);
		initialize();
		startGame();
	}
	/**
     * Initializes the PlayQuiz GUI components and layout.
     * Sets up the game interface including question display, answer options
     * and navigation controls.
     */
	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Changed from EXIT_ON_CLOSE
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		optionGroup = new ButtonGroup();
		
		JLabel titleLabel = new JLabel("GET SET READY QUIZ");
		titleLabel.setForeground(Color.BLUE);
		titleLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		titleLabel.setBounds(250, 10, 209, 51);
		contentPane.add(titleLabel);
		
		difficultyLabel = new JLabel("Difficulty: ");
		difficultyLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		difficultyLabel.setBounds(30, 70, 200, 25);
		contentPane.add(difficultyLabel);
		
		roundLabel = new JLabel("Round: ");
		roundLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		roundLabel.setBounds(286, 70, 150, 25);
		contentPane.add(roundLabel);
		
		scoreLabel = new JLabel("Score: ");
		scoreLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		scoreLabel.setBounds(552, 70, 150, 25);
		contentPane.add(scoreLabel);
		
		questionNumberLabel = new JLabel("Question: ");
		questionNumberLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		questionNumberLabel.setBounds(250, 126, 224, 25);
		contentPane.add(questionNumberLabel);
		
		questionTextArea = new JTextArea();
		questionTextArea.setEditable(false);
		questionTextArea.setLineWrap(true);
		questionTextArea.setWrapStyleWord(true);
		questionTextArea.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
		questionTextArea.setBounds(75, 164, 580, 80);
		contentPane.add(questionTextArea);
		
		optionA = new JRadioButton("");
		optionA.setForeground(new Color(255, 255, 255));
		optionA.setBackground(Color.BLUE);
		optionA.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
		optionA.setBounds(83, 277, 260, 33);
		contentPane.add(optionA);
		
		optionB = new JRadioButton("");
		optionB.setForeground(new Color(255, 255, 255));
		optionB.setBackground(Color.BLUE);
		optionB.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
		optionB.setBounds(412, 273, 260, 35);
		contentPane.add(optionB);
		
		optionC = new JRadioButton("");
		optionC.setForeground(new Color(255, 255, 255));
		optionC.setBackground(Color.BLUE);
		optionC.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
		optionC.setBounds(85, 334, 260, 33);
		contentPane.add(optionC);
		
		optionD = new JRadioButton("");
		optionD.setForeground(new Color(255, 255, 255));
		optionD.setBackground(Color.BLUE);
		optionD.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
		optionD.setBounds(414, 332, 260, 38);
		contentPane.add(optionD);
		
		optionGroup.add(optionA);
		optionGroup.add(optionB);
		optionGroup.add(optionC);
		optionGroup.add(optionD);
		
		nextButton = new JButton("NEXT");
		nextButton.setForeground(new Color(255, 255, 255));
		nextButton.setBackground(Color.BLUE);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processAnswer();
			}
		});
		nextButton.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		nextButton.setBounds(430, 407, 248, 35);
		contentPane.add(nextButton);
		
		JButton back = new JButton("QUIT");
		back.setForeground(Color.WHITE);
		back.setBackground(new Color(255, 0, 0));
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitGame();
			}
		});
		back.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		back.setBounds(45, 415, 100, 35);
		contentPane.add(back);
		
		// Option labels
		JLabel lblOptionA = new JLabel("A:");
		lblOptionA.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionA.setBounds(56, 289, 20, 20);
		contentPane.add(lblOptionA);
		
		JLabel lblOptionB = new JLabel("B:");
		lblOptionB.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionB.setBounds(387, 286, 20, 20);
		contentPane.add(lblOptionB);
		
		JLabel lblOptionC = new JLabel("C:");
		lblOptionC.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionC.setBounds(58, 345, 20, 20);
		contentPane.add(lblOptionC);
		
		JLabel lblOptionD = new JLabel("D:");
		lblOptionD.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionD.setBounds(390, 348, 20, 20);
		contentPane.add(lblOptionD);
	}
	/**
     * Starts the quiz game after validation checks.
     * Verifies sufficient questions are available and initializes the first question.
     */
	private void startGame() {
		if (!quizGame.hasEnoughQuestions()) {
			JOptionPane.showMessageDialog(this, 
				"Not enough questions available for " + quizGame.getDifficulty() + " difficulty!\n" +
				"Minimum " + quizGame.getQuestionsPerRound() + " questions required.\n" +
				"Please ask admin to add more questions.",
				"Insufficient Questions", JOptionPane.WARNING_MESSAGE);
			returnToDashboard();
			return;
		}
		
		updateUI();
		loadCurrentQuestion();
	}
	/**
     * Loads the current question from the quiz game into the UI.
     * Displays question text and answer options. Handles game completion.
     */
	private void loadCurrentQuestion() {
		if (quizGame.isGameComplete()) {
			endGame();
			return;
		}
		
		var question = quizGame.getCurrentQuestion();
		if (question == null) {
			endGame();
			return;
		}
		
		// Display question
		questionTextArea.setText(question.getQuestionText());
		optionA.setText(question.getOptionA());
		optionB.setText(question.getOptionB());
		optionC.setText(question.getOptionC());
		optionD.setText(question.getOptionD());
		
		// Clear selection
		optionGroup.clearSelection();
		updateUI();
	}
	/**
     * Updates all UI labels with current game state information.
     * Refreshes difficulty, round, score and question number displays.
     */
	private void updateUI() {
		difficultyLabel.setText("Difficulty: " + quizGame.getDifficulty());
		roundLabel.setText("Round: " + quizGame.getCurrentRound() + "/" + quizGame.getTotalRounds());
		scoreLabel.setText("Score: " + quizGame.getScore());
		questionNumberLabel.setText("Question " + quizGame.getQuestionNumberInRound() + 
			"/" + quizGame.getQuestionsPerRound() + " in Round " + quizGame.getCurrentRound());
		
		// Update button text
		if (quizGame.isGameComplete()) {
			nextButton.setText("Finish Game");
		} else {
			nextButton.setText("Submit Answer");
		}
	}
	/**
     * Processes the player's answer selection.
     * Validates that an option is selected, submits answer to game logic
     * and handles round completion or game end as needed.
     */
	private void processAnswer() {
		// Check if an option is selected
		String selectedAnswer = getSelectedAnswer();
		if (selectedAnswer == null) {
			JOptionPane.showMessageDialog(this, 
				"Please select an answer!", 
				"No Answer Selected", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Submit answer
		boolean roundComplete = quizGame.submitAnswer(selectedAnswer);
		
		// Update score display
		updateUI();
		
		if (roundComplete) {
			showRoundResults();
		} else if (quizGame.isGameComplete()) {
			endGame();
		} else {
			loadCurrentQuestion();
		}
	}
	/**
     * Gets the currently selected answer option.
     * 
     * @return The selected option (A, B, C or D) or null if no selection
     */
	private String getSelectedAnswer() {
		if (optionA.isSelected()) return "A";
		if (optionB.isSelected()) return "B";
		if (optionC.isSelected()) return "C";
		if (optionD.isSelected()) return "D";
		return null;
	}
	/**
     * Shows round completion results and prompts for next round.
     * Displays round score and total score, asks user to continue or quit.
     */
	private void showRoundResults() {
	    String message = String.format("Round %d Complete!\n\n" +
	        "Round Score: %d/%d\n" +
	        "Total Score: %d/%d\n\n" +
	        "Ready for next round?", 
	        quizGame.getCurrentRound(),
	        quizGame.getRoundScore(), quizGame.getQuestionsPerRound(),
	        quizGame.getScore(), quizGame.getQuestionsAnswered());
	    
	    int choice = JOptionPane.showConfirmDialog(this, 
	        message, 
	        "Round Complete!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
	    
	    if (choice == JOptionPane.YES_OPTION) {
	        quizGame.moveToNextRound();
	        loadCurrentQuestion();
	    } else {
	        // Save progress and exit - only if not already saved
	        if (!quizGame.isGameSaved()) {
	            quizGame.saveGame();
	        }
	        returnToDashboard();
	    }
	}
	/**
     * Ends the game and displays final results.
     * Saves final score to database and shows comprehensive statistics.
     */
	private void endGame() {
	    
	    if (!quizGame.isGameSaved()) {
	        // Save final score
	        quizGame.saveGame();
	    }
	    
	    // Show final results
	    String message = String.format("Quiz Complete!\n\n" +
	        "Difficulty: %s\n" +
	        "Rounds Played: %d/%d\n" +
	        "Questions Answered: %d/%d\n" +
	        "Final Score: %d\n" +
	        "Success Rate: %.2f%%\n\n" +
	        "Congratulations %s!", 
	        quizGame.getDifficulty(),
	        quizGame.getCurrentRound(), quizGame.getTotalRounds(),
	        quizGame.getQuestionsAnswered(), quizGame.getTotalQuestions(),
	        quizGame.getScore(),
	        (quizGame.getScore() * 100.0) / quizGame.getQuestionsAnswered(),
	        quizGame.getUsername());
	    
	    JOptionPane.showMessageDialog(this, 
	        message, 
	        "Game Over!", JOptionPane.INFORMATION_MESSAGE);
	    
	    returnToDashboard();
	}
	/**
     * Handles game quit with confirmation and progress saving.
     * Asks user to confirm quit and saves current progress before exiting.
     */
	private void quitGame() {
		int confirm = JOptionPane.showConfirmDialog(this, 
			"Are you sure you want to quit?\nYour progress will be saved.",
			"Confirm Quit", JOptionPane.YES_NO_OPTION);
		
		if (confirm == JOptionPane.YES_OPTION) {
			quizGame.saveGame();
			returnToDashboard();
		}
	}
	/**
     * Returns to the user dashboard after game completion or quit.
     * Preserves username context for proper dashboard initialization.
     */
	private void returnToDashboard() {
		// Always pass the username when creating a new dashboard
		User_Dashboard_GUI dashboard = new User_Dashboard_GUI(username);
		dashboard.setVisible(true);
		dispose(); // Only dispose the PlayQuiz window
	}
	
	/**
     * Launch the application for testing.
     * Creates a test instance of PlayQuiz with beginner difficulty.
     * 
     * @param args Command line arguments (not used)
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Play_Quiz_GUI frame = new Play_Quiz_GUI("Beginner", "testUser");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}