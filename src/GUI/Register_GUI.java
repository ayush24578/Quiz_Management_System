package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 * Registration window for new users to create accounts in Quiz.
 * Provides interface for users to register with username, password and email.
 * Includes validation, duplicate checking
 * 
 */
public class Register_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPasswordField password;
	private JTextField email;
	private JTextField username;

	/**
     * Launch the application
     * Creates and displays the Registration window.
     * 
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register_GUI frame = new Register_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
     * Create the Registration frame with all UI components.
     * Initializes the registration form with input fields and buttons.
     */
	public Register_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Registration");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblNewLabel.setBounds(306, 28, 180, 39);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_1.setBounds(282, 174, 116, 23);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(282, 246, 116, 23);
		contentPane.add(lblNewLabel_1_1);
		
		password = new JPasswordField();
		password.setBounds(282, 279, 178, 29);
		contentPane.add(password);
		
		email = new JTextField();
		email.setBounds(282, 207, 178, 29);
		contentPane.add(email);
		email.setColumns(10);
		
		JButton register = new JButton("Register");
		register.setForeground(Color.WHITE);
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performRegistration();
			}
		});
		register.setBackground(Color.GREEN);
		register.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		register.setBounds(282, 318, 178, 29);
		contentPane.add(register);
		
		JButton backlogin = new JButton("Previous");
		backlogin.setForeground(Color.WHITE);
		backlogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login_GUI log = new Login_GUI();
				log.setVisible(true);
				dispose();
			}
		});
		backlogin.setBackground(Color.BLUE);
		backlogin.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		backlogin.setBounds(282, 358, 178, 29);
		contentPane.add(backlogin);
		
		JLabel lblNewLabel_1_2 = new JLabel("Username");
		lblNewLabel_1_2.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel_1_2.setBounds(282, 102, 116, 23);
		contentPane.add(lblNewLabel_1_2);
		
		username = new JTextField();
		username.setColumns(10);
		username.setBounds(282, 135, 178, 29);
		contentPane.add(username);
	}
	/**
     * Performs the user registration process with validation.
     * Validates input fields, checks for duplicate usernames and saves to database.
     * Shows appropriate success or error messages to the user.
     */
	private void performRegistration() {
		String enteredUsername = username.getText().trim();
		String enteredPassword = new String(password.getPassword());
		String enteredEmail = email.getText().trim();
		
		// Validation
		if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
			JOptionPane.showMessageDialog(this, 
				"Username and password are required!", 
				"Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (enteredUsername.length() < 3) {
			JOptionPane.showMessageDialog(this, 
				"Username must be at least 3 characters!", 
				"Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (enteredPassword.length() < 4) {
			JOptionPane.showMessageDialog(this, 
				"Password must be at least 4 characters!", 
				"Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Check if username already exists
		if (isUsernameTaken(enteredUsername)) {
			JOptionPane.showMessageDialog(this, 
				"Username already exists! Please choose another one.", 
				"Username Taken", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Save to database
		if (saveUserToDatabase(enteredUsername, enteredPassword, enteredEmail)) {
			JOptionPane.showMessageDialog(this, 
				"Registration successful! You can now login.", 
				"Success", JOptionPane.INFORMATION_MESSAGE);
			Login_GUI login = new Login_GUI();
			login.setVisible(true);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, 
				"Registration failed. Please try again.", 
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
     * Checks if a username is already taken in the database.
     * Queries the database to verify username availability.
     * 
     * @param username The username to check for availability
     * @return true if username exists, false otherwise
     */
	private boolean isUsernameTaken(String username) {
		String url = "jdbc:mysql://localhost:3306/qms";
		String dbUsername = "root";
		String dbPassword = "";
		
		String query = "SELECT id FROM users WHERE username = ?";
		
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
			 PreparedStatement pstmt = conn.prepareStatement(query)) {
			
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return rs.next(); // Returns true if username exists
			
		} catch (SQLException e) {
			System.out.println("Error checking username: " + e.getMessage());
			// In case of error, assume username is not taken to allow registration
			return false;
		}
	}
	/**
     * Saves a new user to the database with 'player' role.
     * Inserts username, password, email and default role into users table.
     * 
     * @param username The new username
     * @param password The user's password
     * @param email The user's email address
     * @return true if registration successful, false otherwise
     */
	private boolean saveUserToDatabase(String username, String password, String email) {
		String url = "jdbc:mysql://localhost:3306/qms";
		String dbUsername = "root";
		String dbPassword = "";
		
		String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'player')";
		
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
			 PreparedStatement pstmt = conn.prepareStatement(query)) {
			
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, email.isEmpty() ? null : email);
			
			int affectedRows = pstmt.executeUpdate();
			System.out.println("Registered new user: " + username);
			return affectedRows > 0;
			
		} catch (SQLException e) {
			System.out.println("Error registering user: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}