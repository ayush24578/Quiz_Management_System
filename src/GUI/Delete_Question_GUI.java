package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

// Import MySQL database classes
import DATABASE.DBFetch;
import DATABASE.DBDelete;
import models.Question;
import java.awt.Color;

/**
 * Delete window for admin to remove questions from the Quiz database.
 * Provides interface to search for questions by ID and delete them after confirmation.
 * Includes validation and safety measures to prevent accidental deletion.
 * 
 */
public class Delete_Question_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField questionIdField;
	private JTextArea questionTextArea;
	private Question currentQuestion;

	/**
     * Launch the application
     * Creates and displays the Delete Questions window.
     * 
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Delete_Question_GUI frame = new Delete_Question_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Delete_Question_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton back_1 = new JButton("Previous");
		back_1.setForeground(Color.WHITE);
		back_1.setBackground(Color.BLUE);
		back_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin_QMS_GUI ad = new Admin_QMS_GUI();
				ad.setVisible(true);
				dispose();
			}
		});
		back_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		back_1.setBounds(10, 23, 125, 27);
		contentPane.add(back_1);
		
		JLabel lblDelete = new JLabel("Delete Question");
		lblDelete.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblDelete.setBounds(261, 23, 170, 27);
		contentPane.add(lblDelete);
		
		JLabel lblQuestionId = new JLabel("Question ID");
		lblQuestionId.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblQuestionId.setBounds(287, 89, 108, 20);
		contentPane.add(lblQuestionId);
		
		questionIdField = new JTextField();
		questionIdField.setColumns(10);
		questionIdField.setBounds(287, 118, 100, 27);
		contentPane.add(questionIdField);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setForeground(Color.WHITE);
		btnSearch.setBackground(Color.GREEN);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchQuestion();
			}
		});
		btnSearch.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		btnSearch.setBounds(232, 155, 100, 27);
		contentPane.add(btnSearch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setForeground(Color.WHITE);
		btnClear.setBackground(Color.GREEN);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearForm();
			}
		});
		btnClear.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		btnClear.setBounds(357, 155, 80, 27);
		contentPane.add(btnClear);
		
		JLabel lblNewLabel_1 = new JLabel("Question");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_1.setBounds(304, 226, 91, 20);
		contentPane.add(lblNewLabel_1);
		
		questionTextArea = new JTextArea();
		questionTextArea.setEditable(false);
		questionTextArea.setBounds(137, 270, 417, 55);
		contentPane.add(questionTextArea);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setForeground(new Color(255, 255, 255));
		btnDelete.setBackground(new Color(255, 0, 0));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteQuestion();
			}
		});
		btnDelete.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		btnDelete.setBounds(458, 361, 96, 32);
		contentPane.add(btnDelete);

	}
	/**
     * Searches for a question by ID in the database.
     * Validates input, queries the database and displays the question if found.
     * Shows error messages for invalid input or non existent questions.
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
			
			// Use DBFetch to get question from database
			currentQuestion = DBFetch.getQuestionById(id);
			
			if (currentQuestion != null) {
				questionTextArea.setText(currentQuestion.getQuestionText());
				JOptionPane.showMessageDialog(this, 
					"Question ID " + id + " found.\nYou can delete if you want to.", 
					"Question Found", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, 
					"Question not found with ID: " + id, 
					"Not Found", JOptionPane.WARNING_MESSAGE);
				clearForm();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID", 
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
     * Deletes the currently displayed question from the database.
     * Shows a confirmation dialog with question details before deletion.
     * Only proceeds if user confirms and question exists in memory.
     */
	private void deleteQuestion() {
		if (currentQuestion == null) {
			JOptionPane.showMessageDialog(this, 
				"Please search for a question first.", 
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Show detailed confirmation with question info
		String message = "Are you sure you want to delete this question?\n\n" +
						"ID: " + currentQuestion.getId() + "\n" +
						"Question: " + currentQuestion.getQuestionText() + "\n" +
						"Difficulty: " + currentQuestion.getDifficulty() + "\n\n" +
						"This action cannot be undone!";
		
		int confirm = JOptionPane.showConfirmDialog(this, 
			message, 
			"Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if (confirm == JOptionPane.YES_OPTION) {
			// Perform delete using DBDelete
			boolean success = DBDelete.deleteQuestion(currentQuestion.getId());
			
			if (success) {
				JOptionPane.showMessageDialog(this, 
					"Question ID " + currentQuestion.getId() + " deleted successfully", 
					"Success", JOptionPane.INFORMATION_MESSAGE);
				clearForm();
			} else {
				JOptionPane.showMessageDialog(this, 
					"Failed to delete question. Please try again.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	/**
     * Clears all form fields and resets the current question reference.
     * Sets focus back to the question ID field for quick entry of next search.
     */
	private void clearForm() {
		questionIdField.setText("");
		questionTextArea.setText("");
		currentQuestion = null;
		questionIdField.requestFocus();
	}
}