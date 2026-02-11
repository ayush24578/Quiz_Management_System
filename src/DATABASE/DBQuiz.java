package DATABASE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Question;

/**
 * Handles quiz-specific database operations including game play, scoring,
 * leaderboards, and player statistics.
 * This class is for quiz game functionality.
 * 
 */

public class DBQuiz {

	/**
     * Retrieves questions based on difficulty level with random ordering.
     * 
     * @param difficulty The difficulty level (Beginner, Intermediate, Advanced)
     * @param limit The maximum number of questions to retrieve
     * @return List of Question objects for the specified difficulty
     * @throws SQLException if a database access error occurs
     */
	
    public static List<Question> getQuestionsByDifficulty(String difficulty, int limit) {
        List<Question> questions = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "SELECT * FROM questions WHERE difficulty_level = ? ORDER BY RAND() LIMIT ?";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, difficulty);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Question question = new Question(
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer"),
                        rs.getString("difficulty_level"));
                question.setId(rs.getInt("question_id"));
                questions.add(question);
            }

            System.out.println("Loaded " + questions.size() + " questions for difficulty: " + difficulty);

        } catch (SQLException e) {
            System.out.println("Error fetching the questions: " + e.getMessage());
            e.printStackTrace();
        }
        return questions;
    }
    /**
     * Saves a player score to the database and update to database.
     * 
     * @param userId The ID of the user
     * @param difficulty The difficulty level played
     * @param round The number of round
     * @param score The score achieved
     * @param totalQuestions Total questions in the round
     * @return true if save successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    
    public static boolean saveScore(int userId, String difficulty, int round,
            int score, int totalQuestions) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "INSERT INTO scores (user_id, difficulty, round_number, score, total_questions) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, difficulty);
            pstmt.setInt(3, round);
            pstmt.setInt(4, score);
            pstmt.setInt(5, totalQuestions);

            int affectedRows = pstmt.executeUpdate();

            // Update player statistics
            if (affectedRows > 0) {
                updatePlayerStats(userId, score, totalQuestions);
                updateDifficultyStats(userId, difficulty, score, totalQuestions); // Sync leaderboard stats
                System.out.println("Score saved for user " + userId + ": " + score + "/" + totalQuestions);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error saving score: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Update player statistics in database.
     * This method is called when a score is saved.
     * 
     * @param userId The ID of the user
     * @param score The score achieved
     * @param totalQuestions Total questions in the round
     * @throws SQLException if a database access error occurs
     */
    
    // Update player statistics
    private static void updatePlayerStats(int userId, int score, int totalQuestions) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            conn.setAutoCommit(false);

            // Check if player stats exist
            String checkQuery = "SELECT * FROM players_stats WHERE user_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update existing stats
                int gamesPlayed = rs.getInt("total_games_played") + 1;
                int totalCorrect = rs.getInt("total_correct_answers") + score;
                int totalWrong = rs.getInt("total_wrong_answers") + (totalQuestions - score);
                double successRate = (totalCorrect * 100.0) / (totalCorrect + totalWrong);

                String updateQuery = "UPDATE players_stats SET " +
                        "total_games_played = ?, " +
                        "total_correct_answers = ?, " +
                        "total_wrong_answers = ?, " +
                        "success_rate = ?, " +
                        "last_active = CURRENT_TIMESTAMP " +
                        "WHERE user_id = ?";

                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, gamesPlayed);
                updateStmt.setInt(2, totalCorrect);
                updateStmt.setInt(3, totalWrong);
                updateStmt.setDouble(4, successRate);
                updateStmt.setInt(5, userId);
                updateStmt.executeUpdate();

            } else {
                String getUsernameQuery = "SELECT username FROM users WHERE id = ?";
                PreparedStatement getUsernameStmt = conn.prepareStatement(getUsernameQuery);
                getUsernameStmt.setInt(1, userId);
                ResultSet userRs = getUsernameStmt.executeQuery();

                if (userRs.next()) {
                    String username = userRs.getString("username");
                    String insertQuery = "INSERT INTO players_stats " +
                            "(user_id, username, total_games_played, total_correct_answers, " +
                            "total_wrong_answers, success_rate, last_active) " +
                            "VALUES (?, ?, 1, ?, ?, ?, CURRENT_TIMESTAMP)";

                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, username);
                    insertStmt.setInt(3, score);
                    insertStmt.setInt(4, totalQuestions - score);
                    insertStmt.setDouble(5, (score * 100.0) / totalQuestions);
                    insertStmt.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Player statistics updated for user: " + userId);

        } catch (SQLException e) {
            System.out.println("Error updating player status: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Updates difficulty-specific statistics for leaderboards.
     * This method is called when a score is saved.
     * 
     * @param userId The ID of the user
     * @param difficulty The difficulty level
     * @param score The score achieved
     * @param totalQuestions Total questions in the round
     * @throws SQLException if a database access error occurs
     */
    
    private static void updateDifficultyStats(int userId, String difficulty, int score, int totalQuestions) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            conn.setAutoCommit(false);

            // Check if stats exist for user and difficulty
            String checkQuery = "SELECT * FROM difficulty_stats WHERE user_id = ? AND difficulty_level = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            checkStmt.setString(2, difficulty);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update existing stats
                int gamesPlayed = rs.getInt("total_games_played") + 1;
                int totalCorrect = rs.getInt("total_correct_answers") + score;
                int totalWrong = rs.getInt("total_wrong_answers") + (totalQuestions - score); // Fix: derived from score
                double successRate = (totalCorrect * 100.0) / (totalCorrect + totalWrong);
                int currentBest = rs.getInt("best_score");
                int newBest = Math.max(currentBest, score);

                String updateQuery = "UPDATE difficulty_stats SET " +
                        "total_games_played = ?, " +
                        "total_correct_answers = ?, " +
                        "total_wrong_answers = ?, " +
                        "success_rate = ?, " +
                        "best_score = ?, " +
                        "last_active = CURRENT_TIMESTAMP " +
                        "WHERE user_id = ? AND difficulty_level = ?";

                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, gamesPlayed);
                updateStmt.setInt(2, totalCorrect);
                updateStmt.setInt(3, totalWrong);
                updateStmt.setDouble(4, successRate);
                updateStmt.setInt(5, newBest);
                updateStmt.setInt(6, userId);
                updateStmt.setString(7, difficulty);
                updateStmt.executeUpdate();

            } else {
                // Insert new stats
                String insertQuery = "INSERT INTO difficulty_stats " +
                        "(user_id, difficulty_level, total_games_played, total_correct_answers, " +
                        "total_wrong_answers, success_rate, best_score, last_active) " +
                        "VALUES (?, ?, 1, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, difficulty);
                insertStmt.setInt(3, score);
                insertStmt.setInt(4, totalQuestions - score);
                insertStmt.setDouble(5, (score * 100.0) / totalQuestions);
                insertStmt.setInt(6, score); // Best score is current score
                insertStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Difficulty stats updated for user: " + userId + ", Diff: " + difficulty);

        } catch (SQLException e) {
            System.out.println("Error updating difficulty stats: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Retrieves all scores for a specific user.
     * 
     * @param userId The ID of the user
     * @return List of score data containing difficulty, round, score, success_rate and date
     * @throws SQLException if a database access error occurs
     */
    
    public static List<String[]> getUserScores(int userId) {
        List<String[]> scores = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "SELECT difficulty, round_number, score, total_questions, date_played " +
                "FROM scores WHERE user_id = ? ORDER BY date_played DESC";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] scoreData = new String[5];
                scoreData[0] = rs.getString("difficulty");
                scoreData[1] = String.valueOf(rs.getInt("round_number"));
                scoreData[2] = rs.getInt("score") + "/" + rs.getInt("total_questions");
                scoreData[3] = String.format("%.2f%%",
                        (rs.getInt("score") * 100.0) / rs.getInt("total_questions"));
                scoreData[4] = rs.getTimestamp("date_played").toString();
                scores.add(scoreData);
            }

            System.out.println("Loaded " + scores.size() + " scores for user: " + userId);

        } catch (SQLException e) {
            System.out.println("Error fetching user scores: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }
    
    /**
     * Retrieves the leaderboard of all players.
     * 
     * @param limit The maximum number of players to return
     * @return List of player data containing rank, username, games played, correct answers, success rate and last active
     * @throws SQLException if a database access error occurs
     */
    
    public static List<String[]> getLeaderboard(int limit) {
        List<String[]> leaderboard = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "SELECT u.username, " +
                "IFNULL(ps.total_games_played, 0) as games_played, " +
                "IFNULL(ps.total_correct_answers, 0) as total_correct, " +
                "IFNULL(ps.success_rate, 0) as success_rate, " +
                "IFNULL(ps.last_active, 'Never') as last_active " +
                "FROM users u " +
                "LEFT JOIN players_stats ps ON u.id = ps.user_id " + 
                "WHERE u.role = 'player' OR u.role = 'user' " +
                "ORDER BY ps.success_rate DESC, ps.total_correct_answers DESC " +
                "LIMIT ?";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            int rank = 1;
            while (rs.next()) {
                String[] playerData = new String[6];
                playerData[0] = String.valueOf(rank++);
                playerData[1] = rs.getString("username");
                playerData[2] = String.valueOf(rs.getInt("games_played"));
                playerData[3] = String.valueOf(rs.getInt("total_correct"));
                playerData[4] = String.format("%.2f%%", rs.getDouble("success_rate"));
                playerData[5] = rs.getString("last_active").toString();
                leaderboard.add(playerData);
            }

            System.out.println("Loaded leaderboard with " + leaderboard.size() + " players");

        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard: " + e.getMessage());
            e.printStackTrace();
        }
        return leaderboard;
    }
    
    /**
     * Retrieves user ID by username.
     * 
     * @param username The username to search for
     * @return User ID if found, 1 (admin ID) as fallback if not found
     * @throws SQLException if a database access error occurs
     */	
    
    public static int getUserId(String username) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                System.out.println("Found user ID " + userId + " for username: " + username);
                return userId;
            } else {
                System.out.println("WARNING: No user found with username: " + username);
                return 1; // Return default user ID (admin) as fallback
            }

        } catch (SQLException e) {
            System.out.println("Error getting user ID: " + e.getMessage());
            e.printStackTrace();
            return 1; // Return default user ID as fallback
        }
    }
    /**
     * Registers a new user in the system.
     * 
     * @param username The desired username
     * @param password The user's password
     * @param email The user's email address
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    
    public static boolean registerUser(String username, String password, String email) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        // Check if username already exists
        if (isUsernameTaken(username)) {
            return false;
        }

        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'player')";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Registered new user: " + username);
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Checks if a username is already taken in the database.
     * 
     * @param username The username to check
     * @return true if username exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    
    private static boolean isUsernameTaken(String username) {
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
            return false;
        }
    }
    /**
     * Clears all scores for a specific user.
     * 
     * @param userId The ID of the user
     * @return true if scores were cleared, false otherwise
     * @throws SQLException if a database access error occurs
     */
    
    public static boolean clearUserScores(int userId) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "DELETE FROM scores WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            int rowsDeleted = pstmt.executeUpdate();

            System.out.println("Cleared " + rowsDeleted + " scores for user ID: " + userId);
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Error clearing user scores: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Retrieves leaderboard filtered by difficulty level.
     * 
     * @param difficulty The difficulty level to filter by (or "All" for all difficulties)
     * @param limit The maximum number of players to return
     * @return List of player data with detailed statistics
     * @throws SQLException if a database access error occurs
     */
    
    public static List<String[]> getLeaderboardByDifficulty(String difficulty, int limit) {
        List<String[]> leaderboard = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "";

        if ("All".equals(difficulty)) {
            query = "SELECT u.username, " +
                    "'All' as difficulty_level, " +
                    "SUM(ps.total_games_played) as total_games_played, " +
                    "SUM(ps.total_correct_answers) as total_correct_answers, " +
                    "AVG(ps.success_rate) as success_rate, " +
                    "MAX(ps.best_score) as best_score, " +
                    "MAX(ps.last_active) as last_active " +
                    "FROM users u " +
                    "LEFT JOIN difficulty_stats ps ON u.id = ps.user_id " +
                    "WHERE u.role = 'player' " +
                    "GROUP BY u.id, u.username " +
                    "ORDER BY success_rate DESC, total_correct_answers DESC " +
                    "LIMIT ?";
        } else {
            query = "SELECT u.username, " +
                    "ps.difficulty_level, " +
                    "ps.total_games_played, " +
                    "ps.total_correct_answers, " +
                    "ps.success_rate, " +
                    "ps.best_score, " +
                    "ps.last_active " +
                    "FROM users u " +
                    "LEFT JOIN difficulty_stats ps ON u.id = ps.user_id " +
                    "WHERE (u.role = 'player' OR u.role = 'user') " +
                    "AND ps.difficulty_level = ? " +
                    "ORDER BY ps.success_rate DESC, ps.total_correct_answers DESC " +
                    "LIMIT ?";
        }

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            if ("All".equals(difficulty)) {
                pstmt.setInt(1, limit);
            } else {
                pstmt.setString(1, difficulty);
                pstmt.setInt(2, limit);
            }

            ResultSet rs = pstmt.executeQuery();

            int rank = 1;
            while (rs.next()) {
                String[] playerData = new String[8];
                playerData[0] = String.valueOf(rank++);
                playerData[1] = rs.getString("username");
                playerData[2] = rs.getString("difficulty_level");
                playerData[3] = String.valueOf(rs.getInt("total_games_played"));
                playerData[4] = String.valueOf(rs.getInt("total_correct_answers"));
                playerData[5] = String.format("%.2f%%", rs.getDouble("success_rate"));
                playerData[6] = String.valueOf(rs.getInt("best_score"));
                Timestamp lastActive = rs.getTimestamp("last_active");
                playerData[7] = (lastActive != null) ? lastActive.toString() : "Never";
                leaderboard.add(playerData);
            }

            System.out.println("Loaded " + leaderboard.size() + " players for difficulty: " + difficulty);

        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard by difficulty: " + e.getMessage());
            e.printStackTrace();
        }
        return leaderboard;
    }
    /**
     * Retrieves a user's rank for a specific difficulty level.
     * 
     * @param username The username to check rank for
     * @param difficulty The difficulty level
     * @return Formatted string containing rank information or error message
     * @throws SQLException if a database access error occurs
     */
    public static String getUserRank(String username, String difficulty) {
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query;
        
        if ("All".equals(difficulty)) {
            // For "All" difficulty level
            query = "SELECT rank, total_players, total_correct, total_wrong FROM (" +
                    "SELECT u.username, " +
                    "SUM(ds.total_correct_answers) as total_correct, " +
                    "SUM(ds.total_wrong_answers) as total_wrong, " +
                    "ROW_NUMBER() OVER (ORDER BY (SUM(ds.total_correct_answers) * 100.0) / (SUM(ds.total_correct_answers) + SUM(ds.total_wrong_answers)) DESC, SUM(ds.total_correct_answers) DESC) as rank, " +
                    "COUNT(*) OVER () as total_players " +
                    "FROM users u " +
                    "LEFT JOIN difficulty_stats ds ON u.id = ds.user_id " +
                    "WHERE (u.role = 'player' OR u.role = 'user') " +
                    "GROUP BY u.id, u.username) ranked " +
                    "WHERE username = ?";
        } else {
            // For specific difficulty level
            query = "SELECT rank, total_players, total_correct_answers, total_wrong_answers FROM (" +
                    "SELECT u.username, ps.difficulty_level, " +
                    "ps.total_correct_answers, ps.total_wrong_answers, " +
                    "ROW_NUMBER() OVER (ORDER BY (ps.total_correct_answers * 100.0) / (ps.total_correct_answers + ps.total_wrong_answers) DESC, ps.total_correct_answers DESC) as rank, " +
                    "COUNT(*) OVER () as total_players " +
                    "FROM users u " +
                    "LEFT JOIN difficulty_stats ps ON u.id = ps.user_id " +
                    "WHERE (u.role = 'player' OR u.role = 'user') " +
                    "AND ps.difficulty_level = ?) ranked " +
                    "WHERE username = ?";
        }

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            if ("All".equals(difficulty)) {
                pstmt.setString(1, username);
            } else {
                pstmt.setString(1, difficulty);
                pstmt.setString(2, username);
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int rank = rs.getInt("rank");
                int totalPlayers = rs.getInt("total_players");
                int totalCorrect = rs.getInt("All".equals(difficulty) ? "total_correct" : "total_correct_answers");
                int totalWrong = rs.getInt("All".equals(difficulty) ? "total_wrong" : "total_wrong_answers");
                
                // Calculate success rate manually
                double successRate = 0;
                if ((totalCorrect + totalWrong) > 0) {
                    successRate = (totalCorrect * 100.0) / (totalCorrect + totalWrong);
                }

                String difficultyText = "All Difficulties".equals(difficulty) ? "All" : difficulty;
                return String.format("Player: %s\nDifficulty: %s\nRank: %d/%d\nSuccess Rate: %.2f%%",
                        username, difficultyText, rank, totalPlayers, successRate);
            } else {
                // No ranking found for this user
                return String.format("No ranking data found for %s in %s difficulty", 
                        username, "All".equals(difficulty) ? "any" : difficulty);
            }

        } catch (SQLException e) {
            System.out.println("Error getting user rank: " + e.getMessage());
            e.printStackTrace();
            return "Error retrieving rank. Please try again.";
        }
    }
    /**
     * Retrieves all players in the system for admin panel.
     * 
     * @return List of player data containing ID, username and email
     * @throws SQLException if a database access error occurs
     */
    
    public static List<String[]> getAllPlayers() {
        List<String[]> players = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "SELECT id, username, email FROM users WHERE role = 'player'";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] player = new String[3];
                player[0] = String.valueOf(rs.getInt("id"));
                player[1] = rs.getString("username");
                player[2] = rs.getString("email");
                players.add(player);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all players: " + e.getMessage());
            e.printStackTrace();
        }
        return players;
    }
    /**
     * Retrieves all statistics for admin panel display.
     * 
     * @return List of statistic arrays containing ID, username and stats
     * @throws SQLException if a database access error occurs
     */
    
    public static List<String[]> getAllStatistics() {
        List<String[]> stats = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        //stats per user
        String query = "SELECT u.id, u.username, " +
                "IFNULL(SUM(ds.total_games_played), 0) as total_games, " +
                "IFNULL(AVG(ds.success_rate), 0) as avg_success " +
                "FROM users u " +
                "LEFT JOIN difficulty_stats ds ON u.id = ds.user_id " +
                "WHERE u.role = 'player' " +
                "GROUP BY u.id, u.username";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] stat = new String[3];
                stat[0] = String.valueOf(rs.getInt("id"));
                stat[1] = rs.getString("username");
                stat[2] = String.format("Games: %d | rate: %.2f%%",
                        rs.getInt("total_games"), rs.getDouble("avg_success"));
                stats.add(stat);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all statistics: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }
    /**
     * Retrieves detailed statistics for a specific player.
     * 
     * @param userId The ID of the user
     * @return Map containing detailed statistics (games played, questions attempted, best score, etc.)
     * @throws SQLException if a database access error occurs
     */
    
    public static java.util.Map<String, String> getPlayerDetailedStats(int userId) {
        java.util.Map<String, String> stats = new java.util.HashMap<>();
        String url = "jdbc:mysql://localhost:3306/qms";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "SELECT " +
                "COUNT(*) as games_played, " +
                "SUM(total_questions) as total_attempts, " +
                "MAX(score) as best_score, " +
                "AVG(score) as avg_score, " +
                "(SELECT date_played FROM scores WHERE user_id = ? ORDER BY score DESC, date_played DESC LIMIT 1) as best_score_time "
                +
                "FROM scores WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stats.put("Games Played", String.valueOf(rs.getInt("games_played")));
                stats.put("Questions Attempted", String.valueOf(rs.getInt("total_attempts")));
                stats.put("Best Score", String.valueOf(rs.getInt("best_score")));
                stats.put("Average Score", String.format("%.2f", rs.getDouble("avg_score")));
                Timestamp ts = rs.getTimestamp("best_score_time");
                stats.put("Best Score Time", ts != null ? ts.toString() : "N/A");
            } else {
                stats.put("Games Played", "0");
                stats.put("Questions Attempted", "0");
                stats.put("Best Score", "0");
                stats.put("Average Score", "0.00");
                stats.put("Best Score Time", "N/A");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching player detailed stats: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }
}