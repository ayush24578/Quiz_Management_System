package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import DATABASE.DBQuiz;
import java.awt.Color;

/**
 * Score View window for players to review their quiz performance history.
 * Displays all quiz scores with detailed statistics including difficulty,
 * round numbers, success rates and timestamps. 
 * Provides ability to clear all scores with confirmation.
 * 
 */
public class Quiz_Score_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable scoresTable;
	private String currentUsername;
	private int userId;

	/**
     * Launch the application
     * Creates and displays the Score View window with test user.
     * 
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Quiz_Score_GUI frame = new Quiz_Score_GUI("testUser");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
     * Creates a Score View window for a specific user.
     * Initializes the interface and loads the user's score history.
     * 
     * @param username The username of the currently logged-in player
     */
	public Quiz_Score_GUI(String username) {
		this.currentUsername = username;
		this.userId = DBQuiz.getUserId(username);
		initialize();
		loadScores();
	}
	/**
     * Initializes the Score View GUI components.
     * Sets up the scores table, navigation buttons and statistics display.
     */
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 532);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton back = new JButton("Previous");
		back.setForeground(new Color(255, 255, 255));
		back.setBackground(Color.BLUE);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User_Dashboard_GUI dashboard = new User_Dashboard_GUI(currentUsername);
				dashboard.setVisible(true);
				dispose();
			}
		});
		back.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		back.setBounds(10, 10, 136, 29);
		contentPane.add(back);
		
		JLabel lblNewLabel = new JLabel("Your Quiz Scores");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblNewLabel.setBounds(270, 10, 190, 29);
		contentPane.add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("Player: " + currentUsername);
		lblUsername.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblUsername.setBounds(280, 45, 300, 25);
		contentPane.add(lblUsername);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 80, 650, 367);
		contentPane.add(scrollPane);
		
		scoresTable = new JTable();
		scoresTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Difficulty", "Round", "Score", "Success Rate", "Date Played"
			}
		) {
			 @Override
			    public boolean isCellEditable(int row, int column) {
			        
			        return false;
			    }
		});
		scrollPane.setViewportView(scoresTable);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setForeground(new Color(255, 255, 255));
		btnRefresh.setBackground(Color.BLUE);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadScores();
			}
		});
		btnRefresh.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		btnRefresh.setBounds(435, 457, 105, 30);
		contentPane.add(btnRefresh);
		
		JButton btnClearAll = new JButton("Clear");
		btnClearAll.setForeground(new Color(255, 255, 255));
		btnClearAll.setBackground(Color.BLUE);
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAllScores();
			}
		});
		btnClearAll.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		btnClearAll.setBounds(550, 457, 120, 30);
		contentPane.add(btnClearAll);
	}
	/**
     * Loads the user's score history from the database into the table.
     * Calculates and displays aggregate statistics including total games
     * and average success rate. 
     * Shows appropriate message if no scores exist.
     */
	private void loadScores() {
		DefaultTableModel model = (DefaultTableModel) scoresTable.getModel();
		model.setRowCount(0);
		
		List<String[]> scores = DBQuiz.getUserScores(userId);
		
		if (scores.isEmpty()) {
			Object[] row = {"No scores yet!", "-", "-", "-", "-"};
			model.addRow(row);
		} else {
			for (String[] score : scores) {
				model.addRow(score);
			}
		}
		
		double totalSuccess = 0;
		int totalGames = scores.size();
		for (String[] score : scores) {
			String successStr = score[3].replace("%", "");
			totalSuccess += Double.parseDouble(successStr);
		}
		
		double averageSuccess = totalGames > 0 ? totalSuccess / totalGames : 0;
		
		JLabel lblStats = new JLabel(String.format("Total Games: %d | Average Success: %.2f%%", 
			totalGames, averageSuccess));
		lblStats.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblStats.setBounds(20, 460, 397, 25);
		contentPane.add(lblStats);
	}
	/**
     * Clears all scores for the current user with confirmation.
     * Shows warning dialog before irreversible deletion. 
     * Updates table after successful deletion or shows error message if deletion fails.
     */
	private void clearAllScores() {
	    // Show confirmation dialog
	    int confirm = JOptionPane.showConfirmDialog(this,
	        "Are you sure you want to delete ALL your scores?\n" +
	        "This action cannot be undone!",
	        "Confirm Delete All Scores",
	        JOptionPane.YES_NO_OPTION,
	        JOptionPane.WARNING_MESSAGE);
	    
	    if (confirm == JOptionPane.YES_OPTION) {
	        // Call the database method to delete scores
	        boolean success = DBQuiz.clearUserScores(userId);
	        
	        if (success) {
	            JOptionPane.showMessageDialog(this,
	                "All your scores have been cleared successfully!",
	                "Scores Cleared",
	                JOptionPane.INFORMATION_MESSAGE);
	            
	            // Refresh the table
	            loadScores();
	        } else {
	            JOptionPane.showMessageDialog(this,
	                "Failed to clear scores. Please try again.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
}