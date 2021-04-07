-- get rid of line 2 when necessary
DROP DATABASE IF EXISTS chess;
CREATE DATABASE chess;
USE chess;

CREATE TABLE User (
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    date_created DATETIME NOT NULL,
    elo INT NOT NULL,
    num_wins INT NOT NULL,
    num_losses INT NOT NULL,
    num_ties INT NOT NULL,
    num_games INT NOT NULL
);

CREATE TABLE Game (
	game_id INT PRIMARY KEY AUTO_INCREMENT,
    white_player_id INT NOT NULL,
    black_player_id INT NOT NULL,
    FOREIGN KEY (white_player_id) REFERENCES User(user_id),
    FOREIGN KEY (black_player_id) REFERENCES User(user_id),
    -- can pass only those 3 strings, any other string will give an error
    game_status ENUM('win', 'loss', 'tie') NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    white_player_elo INT NOT NULL,
    black_player_elo INT NOT NULL
);