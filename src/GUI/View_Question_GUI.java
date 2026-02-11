package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.util.List;

// Import the MySQL database classes
import DATABASE.DBFetch;
import models.Question;
import java.awt.Color;

/**
 * View Questions Window - Search and Display Quiz Questions
 * 
 * This window allows administrators to search for questions by ID
 * and view their details. It also provides an option to view all
 * questions in the database at once.
 * 
 */
public class View_Question_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField questionIdField;
	
	// TextArea fields
	private JTextArea questionTextArea;
	private JTextArea optionATextArea;
	private JTextArea optionBTextArea;
	private JTextArea optionCTextArea;
	private JTextArea optionDTextArea;
	private JTextArea correctOptionTextArea;
	private JTextArea difficultyTextArea;

	/**
	 * Launch the application
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View_Question_GUI frame = new View_Question_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the View Questions frame with all UI components
	 */
	public View_Question_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Search Questions");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblNewLabel.setBounds(265, 10, 211, 32);
		contentPane.add(lblNewLabel);
		
		// Initialize question text area
		questionTextArea = new JTextArea();
		questionTextArea.setEditable(false);
		questionTextArea.setBounds(125, 118, 500, 56);
		contentPane.add(questionTextArea);
		
		JButton back = new JButton("Previous");
		back.setForeground(new Color(255, 255, 255));
		back.setBackground(Color.BLUE);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin_QMS_GUI ad = new Admin_QMS_GUI();
				ad.setVisible(true);
				dispose();
			}
		});
		back.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		back.setBounds(10, 10, 124, 32);
		contentPane.add(back);
		
		questionIdField = new JTextField();
		questionIdField.setFont(new Font("Times New Roman", Font.BOLD, 10));
		questionIdField.setToolTipText("Enter Question ID");
		questionIdField.setBounds(212, 52, 155, 30);
		contentPane.add(questionIdField);
		questionIdField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setForeground(new Color(255, 255, 255));
		btnSearch.setBackground(Color.BLUE);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchQuestion();
			}
		});
		btnSearch.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		btnSearch.setBounds(377, 50, 99, 32);
		contentPane.add(btnSearch);
		
		// Initialize option text areas
		optionATextArea = new JTextArea();
		optionATextArea.setFont(new Font("Times New Roman", Font.BOLD, 12));
		optionATextArea.setEditable(false);
		optionATextArea.setBounds(125, 205, 190, 40);
		contentPane.add(optionATextArea);
		
		optionBTextArea = new JTextArea();
		optionBTextArea.setFont(new Font("Times New Roman", Font.BOLD, 12));
		optionBTextArea.setEditable(false);
		optionBTextArea.setBounds(429, 205, 196, 40);
		contentPane.add(optionBTextArea);
		
		optionCTextArea = new JTextArea();
		optionCTextArea.setFont(new Font("Times New Roman", Font.BOLD, 12));
		optionCTextArea.setEditable(false);
		optionCTextArea.setBounds(125, 269, 190, 40);
		contentPane.add(optionCTextArea);
		
		optionDTextArea = new JTextArea();
		optionDTextArea.setFont(new Font("Times New Roman", Font.BOLD, 12));
		optionDTextArea.setEditable(false);
		optionDTextArea.setBounds(429, 269, 196, 40);
		contentPane.add(optionDTextArea);
		
		correctOptionTextArea = new JTextArea();
		correctOptionTextArea.setFont(new Font("Times New Roman", Font.BOLD, 10));
		correctOptionTextArea.setEditable(false);
		correctOptionTextArea.setBounds(125, 357, 148, 27);
		contentPane.add(correctOptionTextArea);
		
		JLabel lblNewLabel_2 = new JLabel("Correct Option");
		lblNewLabel_2.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2.setBounds(125, 319, 141, 29);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Difficulty");
		lblNewLabel_2_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2_1.setBounds(477, 319, 92, 29);
		contentPane.add(lblNewLabel_2_1);
		
		difficultyTextArea = new JTextArea();
		difficultyTextArea.setFont(new Font("Times New Roman", Font.BOLD, 10));
		difficultyTextArea.setEditable(false);
		difficultyTextArea.setBounds(477, 357, 148, 27);
		contentPane.add(difficultyTextArea);
		
		JLabel lblNewLabel_1_1 = new JLabel("Question");
		lblNewLabel_1_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(125, 92, 107, 16);
		contentPane.add(lblNewLabel_1_1);
		
		JButton btnViewAll = new JButton("View All Questions");
		btnViewAll.setForeground(new Color(255, 255, 255));
		btnViewAll.setBackground(Color.BLUE);
		btnViewAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewAllQuestions();
			}
		});
		btnViewAll.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		btnViewAll.setBounds(272, 420, 204, 40);
		contentPane.add(btnViewAll);
		
		// Add labels for options
		JLabel lblOptionA = new JLabel("A:");
		lblOptionA.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionA.setBounds(79, 225, 20, 20);
		contentPane.add(lblOptionA);
		
		JLabel lblOptionB = new JLabel("B:");
		lblOptionB.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionB.setBounds(399, 225, 20, 20);
		contentPane.add(lblOptionB);
		
		JLabel lblOptionC = new JLabel("C:");
		lblOptionC.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionC.setBounds(79, 289, 20, 20);
		contentPane.add(lblOptionC);
		
		JLabel lblOptionD = new JLabel("D:");
		lblOptionD.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblOptionD.setBounds(398, 289, 20, 20);
		contentPane.add(lblOptionD);
	}
	/**
	 * Searches for a question by ID in the database
	 * Validates input and displays question if found
	 */
	private void searchQuestion() {
		try {
			String input = questionIdField.getText().trim();
			
			if (input.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a Question ID", 
					"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int id = Integer.parseInt(input);
			
			// Use DBFetch to get question from MySQL database
			Question question = DBFetch.getQuestionById(id);
			
			if (question != null) {
				// Display question details
				displayQuestionDetails(question);
				JOptionPane.showMessageDialog(this, 
					"Question found and displayed!", 
					"Success", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, 
					"Question not found with ID: " + id, 
					"Not Found", JOptionPane.WARNING_MESSAGE);
				clearAllFields();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID", 
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Displays question details in the form fields
	 * 
	 * @param question The Question object to display
	 */
	private void displayQuestionDetails(Question question) {
		questionTextArea.setText(question.getQuestionText());
		optionATextArea.setText(question.getOptionA());
		optionBTextArea.setText(question.getOptionB());
		optionCTextArea.setText(question.getOptionC());
		optionDTextArea.setText(question.getOptionD());
		correctOptionTextArea.setText(question.getCorrectOption());
		difficultyTextArea.setText(question.getDifficulty());
		
		// Highlight the correct option
		highlightCorrectOption(question.getCorrectOption());
	}
	/**
	 * Highlights the correct answer option in green
	 * 
	 * @param correctOption The correct option (A, B, C, or D)
	 */
	private void highlightCorrectOption(String correctOption) {
		// Reset all backgrounds first
		optionATextArea.setBackground(java.awt.Color.WHITE);
		optionBTextArea.setBackground(java.awt.Color.WHITE);
		optionCTextArea.setBackground(java.awt.Color.WHITE);
		optionDTextArea.setBackground(java.awt.Color.WHITE);
		
		// Highlight the correct option
		switch (correctOption.toUpperCase()) {
			case "A":
				optionATextArea.setBackground(new java.awt.Color(144, 238, 144)); // Light green
				break;
			case "B":
				optionBTextArea.setBackground(new java.awt.Color(144, 238, 144));
				break;
			case "C":
				optionCTextArea.setBackground(new java.awt.Color(144, 238, 144));
				break;
			case "D":
				optionDTextArea.setBackground(new java.awt.Color(144, 238, 144));
				break;
		}
	}
	/**
	 * Clears all form fields and resets backgrounds
	 */
	private void clearAllFields() {
		questionTextArea.setText("");
		optionATextArea.setText("");
		optionBTextArea.setText("");
		optionCTextArea.setText("");
		optionDTextArea.setText("");
		correctOptionTextArea.setText("");
		difficultyTextArea.setText("");
		
		// Reset backgrounds
		optionATextArea.setBackground(java.awt.Color.WHITE);
		optionBTextArea.setBackground(java.awt.Color.WHITE);
		optionCTextArea.setBackground(java.awt.Color.WHITE);
		optionDTextArea.setBackground(java.awt.Color.WHITE);
	}
	/**
	 * Displays all questions from the database
	 * Shows question ID, text, options and correct answer
	 */
	private void viewAllQuestions() {
		// Get all questions from MySQL database
		List<Question> allQuestions = DBFetch.getAllQuestions();
		
		if (allQuestions.isEmpty()) {
			JOptionPane.showMessageDialog(this, 
				"No questions found in the database.", 
				"Empty Database", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// Create a simple dialog to show all questions
		StringBuilder sb = new StringBuilder();
		sb.append("=== All Questions ===\n\n");
		sb.append("Total Questions: ").append(allQuestions.size()).append("\n\n");
		
		for (Question q : allQuestions) {
			sb.append("ID: ").append(q.getId()).append("\n");
			sb.append("Difficulty: ").append(q.getDifficulty()).append("\n");
			sb.append("Question: ").append(q.getQuestionText()).append("\n");
			sb.append("Options: A) ").append(q.getOptionA())
			  .append(" B) ").append(q.getOptionB())
			  .append(" C) ").append(q.getOptionC())
			  .append(" D) ").append(q.getOptionD()).append("\n");
			sb.append("Correct: ").append(q.getCorrectOption()).append("\n");
			sb.append("---------------------------------\n");
		}
		
		// Create a scrollable text area for displaying
		JTextArea displayArea = new JTextArea(sb.toString());
		displayArea.setEditable(false);
		displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));
		
		JOptionPane.showMessageDialog(this, scrollPane, 
			"All Questions (" + allQuestions.size() + " total)", 
			JOptionPane.INFORMATION_MESSAGE);
	}
}