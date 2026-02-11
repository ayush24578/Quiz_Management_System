package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

/**
 * Player Management window for administrators in Quiz.
 * Provides interface to view all registered players and their detailed statistics.
 * Shows player list with selection capability and displays comprehensive
 * performance metrics for selected players.
 * 
 */
public class Players_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel gamesPlayedLabel;
	private JLabel attemptsLabel;
	private JLabel bestScoreLabel;
	private JLabel avgScoreLabel;
	private JLabel bestTimeLabel;

	/**
     * Launch the application
     * Creates and displays the Players Management window.
     * 
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Players_GUI frame = new Players_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
     * Create the Players Management frame with all UI components.
     * Initializes the player list table and statistics display panel.
     */
	public Players_GUI() {
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
				Admin_QMS_GUI ap = new Admin_QMS_GUI();
				ap.setVisible(true);
				dispose();
			}
		});
		back.setFont(new Font("Times New Roman", Font.BOLD, 15));
		back.setBounds(10, 10, 119, 29);
		contentPane.add(back);

		JLabel lblNewLabel = new JLabel("Players");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblNewLabel.setBounds(292, 10, 100, 29);
		contentPane.add(lblNewLabel);

		// Table setup
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane();
		scrollPane.setBounds(30, 60, 648, 200);
		contentPane.add(scrollPane);

		javax.swing.JTable table = new javax.swing.JTable();
		table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		table.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {},
				new String[] { "ID", "Username", "Email" }
			) {
				@Override
			    public boolean isCellEditable(int row, int column) {
			        return false;
			    }
			});
		scrollPane.setViewportView(table);

		// Details Panel
		JPanel detailsPanel = new JPanel();
		detailsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Player Statistics",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 14),
				new Color(0, 0, 0)));
		detailsPanel.setBounds(30, 280, 648, 205);
		contentPane.add(detailsPanel);
		detailsPanel.setLayout(null);

		JLabel lblGames = new JLabel("Games Played:");
		lblGames.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		lblGames.setBounds(20, 30, 120, 25);
		detailsPanel.add(lblGames);

		gamesPlayedLabel = new JLabel("-");
		gamesPlayedLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		gamesPlayedLabel.setBounds(182, 30, 200, 25);
		detailsPanel.add(gamesPlayedLabel);

		JLabel lblAttempts = new JLabel("Questions Attempted:");
		lblAttempts.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		lblAttempts.setBounds(20, 65, 152, 25);
		detailsPanel.add(lblAttempts);

		attemptsLabel = new JLabel("-");
		attemptsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		attemptsLabel.setBounds(182, 65, 200, 25);
		detailsPanel.add(attemptsLabel);

		JLabel lblBestScore = new JLabel("Best Score:");
		lblBestScore.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		lblBestScore.setBounds(20, 100, 106, 25);
		detailsPanel.add(lblBestScore);

		bestScoreLabel = new JLabel("-");
		bestScoreLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		bestScoreLabel.setBounds(182, 100, 200, 25);
		detailsPanel.add(bestScoreLabel);

		JLabel lblAvgScore = new JLabel("Average Score:");
		lblAvgScore.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		lblAvgScore.setBounds(20, 135, 120, 25);
		detailsPanel.add(lblAvgScore);

		avgScoreLabel = new JLabel("-");
		avgScoreLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		avgScoreLabel.setBounds(182, 135, 200, 25);
		detailsPanel.add(avgScoreLabel);

		JLabel lblBestTime = new JLabel("Best Score Time:");
		lblBestTime.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		lblBestTime.setBounds(20, 170, 120, 25);
		detailsPanel.add(lblBestTime);

		bestTimeLabel = new JLabel("-");
		bestTimeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		bestTimeLabel.setBounds(182, 170, 260, 25);
		detailsPanel.add(bestTimeLabel);

		// Selection Listener
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					int selectedRow = table.getSelectedRow();
					if (selectedRow != -1) {
						String userIdStr = table.getValueAt(selectedRow, 0).toString();
						int userId = Integer.parseInt(userIdStr);
						updateDetails(userId);
					}
				}
			}
		});

		// Load data
		loadPlayers(table);
	}
	/**
     * Loads all players from the database into the table.
     * Retrieves player list including ID, username and email.
     * 
     * @param table The JTable component to display with player data
     */
	private void loadPlayers(javax.swing.JTable table) {
		javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
		model.setRowCount(0);

		java.util.List<String[]> players = DATABASE.DBQuiz.getAllPlayers();
		for (String[] player : players) {
			model.addRow(player);
		}
	}
	/**
     * Updates the statistics panel with detailed information for a selected player.
     * Retrieves performance metrics from the database and displays them.
     * 
     * @param userId The ID of the player to display statistics
     */
	private void updateDetails(int userId) {
		java.util.Map<String, String> stats = DATABASE.DBQuiz.getPlayerDetailedStats(userId);
		gamesPlayedLabel.setText(stats.getOrDefault("Games Played", "0"));
		attemptsLabel.setText(stats.getOrDefault("Questions Attempted", "0"));
		bestScoreLabel.setText(stats.getOrDefault("Best Score", "0"));
		avgScoreLabel.setText(stats.getOrDefault("Average Score", "0.00"));
		bestTimeLabel.setText(stats.getOrDefault("Best Score Time", "N/A"));
	}
}