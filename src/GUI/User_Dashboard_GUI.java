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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 * User Dashboard - Main Menu After Login
 * 
 * This window is displayed after a user successfull log in to Quiz.
 * It provides access to all main features of the quiz application through
 * simple buttons and dropdown menus.
 * 
 */
public class User_Dashboard_GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String currentUsername;
    private JComboBox<String> difficultyComboBox;

    /**
     * Launch the application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    User_Dashboard_GUI frame = new User_Dashboard_GUI("testUser");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame with username.
     */
    public User_Dashboard_GUI(String username) {
        this.currentUsername = username;
        initialize();
        
        
    }
    
    /**
     * Create dashboard for a guest user
     */
    public User_Dashboard_GUI() {
        this("Guest");
    }
    /**
     * Set up all the UI components for the dashboard
     */
    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 532);
        contentPane = new JPanel();
        contentPane.setBackground(Color.PINK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel welcome = new JLabel("Welcome " + currentUsername + ",");
        welcome.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        welcome.setBounds(264, 39, 250, 40);
        contentPane.add(welcome);
        
        JButton logout = new JButton("Log Out");
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login_GUI log = new Login_GUI();
                log.setVisible(true);
                dispose();
            }
        });
        logout.setForeground(Color.WHITE);
        logout.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        logout.setBackground(Color.RED);
        logout.setBounds(587, 10, 119, 40);
        contentPane.add(logout);
        
        JButton play = new JButton("Play Quiz");
        play.setBackground(Color.GREEN);
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                if ("Select Difficulty".equals(selectedDifficulty)) {
                    JOptionPane.showMessageDialog(User_Dashboard_GUI.this, 
                        "Please select a difficulty level first!", 
                        "Select Difficulty", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // FIXED: Pass parameters to PlayQuiz
                Play_Quiz_GUI playQuiz = new Play_Quiz_GUI(selectedDifficulty, currentUsername);
                playQuiz.setVisible(true);
                setVisible(false);
            }
        });
        play.setForeground(new Color(255, 255, 255));
        play.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        play.setBounds(238, 231, 198, 64);
        contentPane.add(play);
        
        JButton viewScore = new JButton("View Score");
        viewScore.setForeground(new Color(255, 255, 255));
        viewScore.setBackground(Color.BLUE);
        viewScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Quiz_Score_GUI scoreView = new Quiz_Score_GUI(currentUsername);
                scoreView.setVisible(true);
                dispose();
            }
        });
        viewScore.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        viewScore.setBounds(36, 368, 186, 51);
        contentPane.add(viewScore);
        
        difficultyComboBox = new JComboBox<>();
        difficultyComboBox.setForeground(new Color(255, 255, 255));
        difficultyComboBox.setBackground(Color.BLUE);
        difficultyComboBox.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        difficultyComboBox.setModel(new DefaultComboBoxModel<String>(
            new String[] {"Select Difficulty", "Beginner", "Intermediate", "Advanced"}));
        difficultyComboBox.setBounds(238, 160, 192, 40);
        contentPane.add(difficultyComboBox);
        
        JButton leaderboard = new JButton("Leaderboard");
        leaderboard.setForeground(new Color(255, 255, 255));
        leaderboard.setBackground(Color.BLUE);
        leaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leaderboard_GUI leaderboardGUI = new Leaderboard_GUI(currentUsername);
                leaderboardGUI.setVisible(true);
                dispose();
            }
        });
        leaderboard.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        leaderboard.setBounds(468, 368, 205, 51);
        contentPane.add(leaderboard);
        
        JLabel lblNewLabel_1 = new JLabel("GET SET READY QUIZ");
        lblNewLabel_1.setForeground(Color.BLUE);
        lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
        lblNewLabel_1.setBounds(233, 89, 218, 51);
        contentPane.add(lblNewLabel_1);
        

    }
}