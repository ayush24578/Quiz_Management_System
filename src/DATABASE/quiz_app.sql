-- Quiz Management System Database Schema
-- Database: quiz_app

CREATE DATABASE IF NOT EXISTS quiz_app;
USE quiz_app;

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'player'
);

-- Table: questions
CREATE TABLE IF NOT EXISTS questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_answer VARCHAR(1) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL
);

-- Table: scores
CREATE TABLE IF NOT EXISTS scores (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    round_number INT NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table: players_stats
CREATE TABLE IF NOT EXISTS players_stats (
    user_id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    total_games_played INT DEFAULT 0,
    total_correct_answers INT DEFAULT 0,
    total_wrong_answers INT DEFAULT 0,
    success_rate DOUBLE DEFAULT 0.0,
    last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table: difficulty_stats
CREATE TABLE IF NOT EXISTS difficulty_stats (
    user_id INT NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    total_games_played INT DEFAULT 0,
    total_correct_answers INT DEFAULT 0,
    total_wrong_answers INT DEFAULT 0,
    success_rate DOUBLE DEFAULT 0.0,
    best_score INT DEFAULT 0,
    last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, difficulty_level),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert a default admin user for testing
-- Note: Replace 'admin123' with a hashed password in production
INSERT INTO users (username, password, email, role) 
VALUES ('admin', 'admin123', 'admin@quizapp.com', 'admin')
ON DUPLICATE KEY UPDATE username=username;

-- Insert some sample questions
INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer, difficulty_level) VALUES
('What is the capital of France?', 'London', 'Berlin', 'Paris', 'Madrid', 'C', 'Beginner'),
('Which planet is known as the Red Planet?', 'Mars', 'Jupiter', 'Venus', 'Saturn', 'A', 'Beginner'),
('Who wrote "Romeo and Juliet"?', 'Charles Dickens', 'William Shakespeare', 'Mark Twain', 'Jane Austen', 'B', 'Beginner'),
('What is the largest ocean on Earth?', 'Atlantic Ocean', 'Indian Ocean', 'Arctic Ocean', 'Pacific Ocean', 'D', 'Beginner'),
('What is the chemical symbol for Gold?', 'Ag', 'Au', 'Fe', 'Cu', 'B', 'Intermediate');
