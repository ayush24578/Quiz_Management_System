package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import DATABASE.DBLogin;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 * Login window for the Quiz application.
 * Provides a user interface for users to enter credentials and log in.
 * Authenticates users from the database and redirects to dashboard.
 * 
 */

public class Login_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;

	/**
	 * Launch the application.
	 *  @param args Command line arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_GUI frame = new Login_GUI();
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
	public Login_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel loginlbl = new JLabel("Log In");
		loginlbl.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		loginlbl.setBounds(316, 58, 72, 32);
		contentPane.add(loginlbl);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel.setBounds(133, 146, 114, 24);
		contentPane.add(lblNewLabel);
		
		username = new JTextField();
		username.setBounds(266, 150, 222, 24);
		contentPane.add(username);
		username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblPassword.setBounds(133, 201, 114, 24);
		contentPane.add(lblPassword);
		
		password = new JPasswordField();
		password.setBounds(266, 201, 222, 24);
		contentPane.add(password);
		
		JButton login = new JButton("Login");
		login.setForeground(Color.WHITE);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performLogin();
			}
		});
		login.setBackground(new Color(0, 255, 0));
		login.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		login.setBounds(375, 254, 114, 32);
		contentPane.add(login);
		
		JLabel lblNewLabel_1 = new JLabel("Don't Have an Account? Register Here.");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_1.setForeground(Color.BLUE);
		lblNewLabel_1.setBackground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(223, 308, 270, 20);
		contentPane.add(lblNewLabel_1);
		
		JButton register = new JButton("Register");
		register.setForeground(Color.WHITE);
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Register_GUI reg = new Register_GUI();
				reg.setVisible(true);
				dispose();
			}
		});
		register.setBackground(new Color(0, 128, 255));
		register.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		register.setBounds(294, 338, 114, 32);
		contentPane.add(register);
	}
	
	/**
     * Performs login authentication when user clicks Login button.
     * Validates input, checks credentials from database and redirect user.
     * Shows appropriate error messages for failed login attempts.
     */
	
	private void performLogin() {
	    String enteredUsername = username.getText().trim();
	    String enteredPassword = new String(password.getPassword()).trim();

	    //Check for empty inputs
	    if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
	        JOptionPane.showMessageDialog(this, 
	            "Please enter both username and password",
	            "Input Required", 
	            JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    //authentication
	    if (DBLogin.authenticate(enteredUsername, enteredPassword)) {
	        // Success
	        String role = DBLogin.getUserRole(enteredUsername);

	        JOptionPane.showMessageDialog(this, 
	            "Login successful!", 
	            "Welcome", 
	            JOptionPane.INFORMATION_MESSAGE);

	        if ("admin".equals(role)) {
	            Admin_QMS_GUI adminPanel = new Admin_QMS_GUI();
	            adminPanel.setVisible(true);
	        } else {
	            User_Dashboard_GUI userDashboard = new User_Dashboard_GUI(enteredUsername);
	            userDashboard.setVisible(true);
	        }
	        
	        dispose();
	    } 
	    else {
	        JOptionPane.showMessageDialog(this, 
	            "Incorrect username or password", 
	            "Login Failed", 
	            JOptionPane.ERROR_MESSAGE);
	        password.setText("");
	        password.requestFocus();
	    }
	}
}