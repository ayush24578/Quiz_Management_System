package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Main dashboard for admin in the Quiz application.
 * Provides navigation to all administrative functions including
 * question management, player statistics, and system administration.
 * 
 */

public class Admin_QMS_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
     * Launch the application
     * Creates and displays the Admin Panel window.
     * 
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin_QMS_GUI frame = new Admin_QMS_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
     * Create the Admin Panel frame with all navigation buttons.
     * Initializes the main dashboard interface for administrators
     * with access to CRUD operations, statistics, and player management.
     */
	public Admin_QMS_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("(Admin)");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblNewLabel.setBounds(442, 69, 89, 37);
		contentPane.add(lblNewLabel);
		
		// Add button
		JButton add = new JButton("Add");
		add.setForeground(new Color(255, 255, 255));
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Add_Question_GUI ad = new Add_Question_GUI();
				ad.setVisible(true);
				dispose();
			}
		});
		add.setBackground(Color.GREEN);
		add.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		add.setBounds(235, 140, 246, 29);
		contentPane.add(add);
		
		//View button
		JButton view = new JButton("View");
		view.setForeground(new Color(255, 255, 255));
		view.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				View_Question_GUI v = new View_Question_GUI();
				v.setVisible(true);
				dispose();
			}
		});
		view.setBackground(Color.GREEN);
		view.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		view.setBounds(235, 218, 246, 29);
		contentPane.add(view);
		
		// Update button
		JButton Update = new JButton("Update");
		Update.setForeground(new Color(255, 255, 255));
		Update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Update_Question_GUI up = new Update_Question_GUI();
				up.setVisible(true);
				dispose();
			}
		});
		Update.setBackground(Color.GREEN);
		Update.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		Update.setBounds(235, 179, 246, 29);
		contentPane.add(Update);
		
		//delete
		JButton delete = new JButton("Delete");
		delete.setForeground(new Color(255, 255, 255));
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Delete_Question_GUI del = new Delete_Question_GUI();
				del.setVisible(true);
				dispose();
			}
		});
		delete.setBackground(Color.GREEN);
		delete.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		delete.setBounds(235, 262, 246, 29);
		contentPane.add(delete);
		
		JLabel lblNewLabel_2 = new JLabel("Quiz Management System");
		lblNewLabel_2.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblNewLabel_2.setBounds(188, 59, 268, 56);
		contentPane.add(lblNewLabel_2);
		
		// Logout button
		JButton logout = new JButton("Log Out");
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login_GUI log = new Login_GUI();
				log.setVisible(true);
				dispose();
			}
		});
		logout.setForeground(new Color(255, 255, 255));
		logout.setBackground(new Color(255, 0, 0));
		logout.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		logout.setBounds(315, 384, 101, 29);
		contentPane.add(logout);
		
		JButton stats = new JButton("View Statistics");
		stats.setForeground(new Color(255, 255, 255));
		stats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Statistics_GUI stats = new Statistics_GUI();
				stats.setVisible(true);
				dispose();
			}
		});
		stats.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		stats.setBackground(Color.BLUE);
		stats.setFont(new Font("Times New Roman", Font.BOLD, 16));
		stats.setBounds(536, 327, 137, 28);
		contentPane.add(stats);
		
		JButton search = new JButton("Players");
		search.setForeground(new Color(255, 255, 255));
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Players_GUI player = new Players_GUI();
				player.setVisible(true);
				dispose();
			}
		});
		search.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		search.setBackground(Color.BLUE);
		search.setFont(new Font("Times New Roman", Font.BOLD, 16));
		search.setBounds(28, 327, 137, 28);
		contentPane.add(search);

	}
}