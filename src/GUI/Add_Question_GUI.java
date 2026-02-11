package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

// Import the MySQL database classes
import DATABASE.DBInsertion;
import java.awt.Color;
/**
 * Add window for admin to add question to database for Quiz.
 * Provides interface for admin to enter the questions, options, correct option and difficulty level.
 * Validates input before adding the question to the database.
 * 
 */
public class Add_Question_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea questiontextArea;
	private JTextArea optionA;
	private JTextArea optionB;
	private JTextArea optionC;
	private JTextArea optionD;
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;

	/**
	 * Launch the application.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Add_Question_GUI frame = new Add_Question_GUI();
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
	public Add_Question_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Add Questions");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblNewLabel.setBounds(283, 30, 143, 30);
		contentPane.add(lblNewLabel);
		
		JButton back_1 = new JButton("Previous");
		back_1.setForeground(Color.WHITE);
		back_1.setBackground(Color.BLUE);
		back_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin_QMS_GUI ap = new Admin_QMS_GUI();
				ap.setVisible(true);
				dispose();
			}
		});
		back_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		back_1.setBounds(10, 10, 118, 31);
		contentPane.add(back_1);
		
		JLabel lblNewLabel_1 = new JLabel("Question");
		lblNewLabel_1.setBackground(Color.PINK);
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_1.setBounds(37, 51, 91, 20);
		contentPane.add(lblNewLabel_1);
		
		questiontextArea = new JTextArea();
		questiontextArea.setBounds(37, 81, 669, 69);
		contentPane.add(questiontextArea);
		
		JLabel lblNewLabel_1_1 = new JLabel("Options");
		lblNewLabel_1_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(37, 160, 91, 20);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("A");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2.setBounds(37, 195, 24, 30);
		contentPane.add(lblNewLabel_2);
		
		optionA = new JTextArea();
		optionA.setBounds(59, 190, 209, 35);
		contentPane.add(optionA);
		
		optionB = new JTextArea();
		optionB.setBounds(484, 190, 197, 35);
		contentPane.add(optionB);
		
		JLabel lblNewLabel_2_1 = new JLabel("B");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2_1.setBounds(450, 195, 24, 30);
		contentPane.add(lblNewLabel_2_1);
		
		optionC = new JTextArea();
		optionC.setBounds(59, 235, 209, 35);
		contentPane.add(optionC);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("C");
		lblNewLabel_2_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_1_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2_1_1.setBounds(37, 240, 24, 30);
		contentPane.add(lblNewLabel_2_1_1);
		
		optionD = new JTextArea();
		optionD.setBounds(484, 235, 197, 35);
		contentPane.add(optionD);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("D");
		lblNewLabel_2_1_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_1_1_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2_1_1_1.setBounds(450, 240, 24, 30);
		contentPane.add(lblNewLabel_2_1_1_1);
		
		JLabel lblNewLabel_2_2 = new JLabel("Correct Option");
		lblNewLabel_2_2.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2_2.setBounds(53, 328, 143, 29);
		contentPane.add(lblNewLabel_2_2);
		
		JLabel lblNewLabel_2_2_1 = new JLabel("Difficulty");
		lblNewLabel_2_2_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_2_2_1.setBounds(303, 328, 91, 29);
		contentPane.add(lblNewLabel_2_2_1);
		
		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Select Option", "A", "B", "C", "D"}));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(32, 367, 187, 30);
		contentPane.add(comboBox);
		
		comboBox_1 = new JComboBox<>();
		comboBox_1.setModel(new DefaultComboBoxModel<>(new String[] {"Select Difficulty", "Beginner", "Intermediate", "Advanced"}));
		comboBox_1.setSelectedIndex(0);
		comboBox_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		comboBox_1.setBounds(270, 369, 172, 30);
		contentPane.add(comboBox_1);
		
		JButton add = new JButton("Add");
		add.setForeground(Color.WHITE);
		add.setBackground(Color.GREEN);
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addQuestionToDatabase();
			}
		});
		add.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		add.setBounds(517, 367, 84, 32);
		contentPane.add(add);
	}
	/**
     * Validates input and adds a new question to the database.
     * Performs validation checks on all fields before attempting database insertion.
     * Shows appropriate success or error messages to the user.
     */
	private void addQuestionToDatabase() {
		// Validate inputs
		String questionText = questiontextArea.getText().trim();
		String optA = optionA.getText().trim();
		String optB = optionB.getText().trim();
		String optC = optionC.getText().trim();
		String optD = optionD.getText().trim();
		String correctOpt = (String) comboBox.getSelectedItem();
		String difficulty = (String) comboBox_1.getSelectedItem();
		
		// Validation checks
		if (questionText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the question", 
				"Error", JOptionPane.ERROR_MESSAGE);
			questiontextArea.requestFocus();
			return;
		}
		
		if (optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter all options", 
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if ("Select Option".equals(correctOpt)) {
			JOptionPane.showMessageDialog(this, "Please select the correct option", 
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if ("Select Difficulty".equals(difficulty)) {
			JOptionPane.showMessageDialog(this, "Please select difficulty level", 
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Insert question into database and get the generated question ID
		int questionId = DBInsertion.insertQuestion(
			questionText, optA, optB, optC, optD, correctOpt, difficulty
		);
		
		if (questionId > 0) {
			// Show success message with question ID
			JOptionPane.showMessageDialog(this, 
				"Question added successfully!\n" +
				"Question ID: " + questionId, 
				"Success", JOptionPane.INFORMATION_MESSAGE);
			
			// Clear form
			clearForm();
		} else {
			JOptionPane.showMessageDialog(this, 
				"Failed to add question. Please check database connection.", 
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
     * Clears all form fields
     * Sets focus back to the question text area for quick entry of next question.
     */
	private void clearForm() {
		questiontextArea.setText("");
		optionA.setText("");
		optionB.setText("");
		optionC.setText("");
		optionD.setText("");
		comboBox.setSelectedIndex(0);
		comboBox_1.setSelectedIndex(0);
		questiontextArea.requestFocus();
	}
}