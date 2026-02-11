package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import DATABASE.DBQuiz;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;

/**
 * Leaderboard window displaying player rankings and statistics for Quiz.
 * Shows player rankings filtered by difficulty level with ability to view
 * individual player ranks. Provides real-time statistics and comparisons.
 * 
 */
public class Leaderboard_GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable leaderboardTable;
    private String currentUsername;
    private JComboBox<String> difficultyComboBox;

    /**
     * Launch the application
     * Creates and displays the Leaderboard window with test user.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Leaderboard_GUI frame = new Leaderboard_GUI("testUser");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Creates a Leaderboard window
     * Initializes the interface and loads leaderboard data.
     * 
     * @param username The username of the currently logged-in player
     */
    public Leaderboard_GUI(String username) {
        this.currentUsername = username;
        initialize();
        loadLeaderboard("All");
    }
    
    /**
     * Default constructor for compatibility
     * Creates a Leaderboard window with "Guest" user.
     */
    public Leaderboard_GUI() {
        this("Guest");
    }
    /**
     * Initializes the Leaderboard GUI components.
     * Sets up the table, filter controls and navigation buttons.
     */
    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 550); // Increased width
        contentPane = new JPanel();
        contentPane.setBackground(Color.PINK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JButton back = new JButton("Previous");
        back.setForeground(Color.WHITE);
        back.setBackground(Color.BLUE);
        back.setBounds(10, 12, 114, 32);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                User_Dashboard_GUI dashboard = new User_Dashboard_GUI(currentUsername);
                dashboard.setVisible(true);
                dispose();
            }
        });
        back.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        contentPane.add(back);
        
        JLabel lblNewLabel = new JLabel("Quiz Leaderboard");
        lblNewLabel.setBounds(319, 5, 188, 39);
        lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
        contentPane.add(lblNewLabel);
        
        // Difficulty filter
        JLabel lblFilter = new JLabel("Filter by Difficulty:");
        lblFilter.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        lblFilter.setBounds(20, 50, 176, 25);
        contentPane.add(lblFilter);
        
        difficultyComboBox = new JComboBox<>();
        difficultyComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "All", "Beginner", "Intermediate", "Advanced"
        }));
        difficultyComboBox.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        difficultyComboBox.setBounds(206, 50, 129, 25);
        difficultyComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                loadLeaderboard(selectedDifficulty);
            }
        });
        contentPane.add(difficultyComboBox);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 90, 800, 350);
        contentPane.add(scrollPane);
        
        leaderboardTable = new JTable();
        leaderboardTable.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        leaderboardTable.setModel(new DefaultTableModel(
            new Object[][] {
            },
            new String[] {
                "Rank", "Player Name", "Difficulty", "Games Played", "Total Correct", 
                "Success Rate", "Best Score", "Last Active"
            }
        ) {
        	@Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        });
        scrollPane.setViewportView(leaderboardTable);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setForeground(new Color(255, 255, 255));
        btnRefresh.setBackground(Color.BLUE);
        btnRefresh.setBounds(700, 41, 120, 43);
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                loadLeaderboard(selectedDifficulty);
            }
        });
        btnRefresh.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        contentPane.add(btnRefresh);
        
        // Add "View Your Rank" button
        JButton btnYourRank = new JButton("View Your Rank");
        btnYourRank.setForeground(new Color(255, 255, 255));
        btnYourRank.setBackground(Color.GREEN);
        btnYourRank.setBounds(304, 450, 229, 40);
        btnYourRank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showUserRank();
            }
        });
        btnYourRank.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        contentPane.add(btnYourRank);
    }
    /**
     * Loads leaderboard data from the database and populates the table.
     * Filters results by difficulty level and handles loading errors gracefully.
     * 
     * @param difficulty The difficulty level to filter by ("All", "Beginner", "Intermediate", "Advanced")
     */
    private void loadLeaderboard(String difficulty) {
        DefaultTableModel model = (DefaultTableModel) leaderboardTable.getModel();
        model.setRowCount(0);
        
        try {
            List<String[]> leaderboard = DBQuiz.getLeaderboardByDifficulty(difficulty, 50);
            
            for (String[] player : leaderboard) {
                model.addRow(player);
            }
            
        } catch (Exception e) {
            // Show error in table
            Object[] errorRow = {
                "Error", 
                "Could not load leaderboard: " + e.getMessage(),
                difficulty,
                "-", "-", "-", "-", "-"
            };
            model.addRow(errorRow);
            e.printStackTrace();
        }
    }
    /**
     * Displays the current user's ranking in a dialog box.
     * Shows rank position, difficulty level and success rate.
     * Handles cases where user has no ranking data.
     */
    private void showUserRank() {
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        String userRank = DBQuiz.getUserRank(currentUsername, difficulty);
        
        if (userRank != null && !userRank.contains("No ranking data")) {
            JOptionPane.showMessageDialog(this, 
                userRank,
                "Your Ranking", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                userRank != null ? userRank : "No ranking data found for " + currentUsername + " in " + difficulty,
                "No Ranking", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
}