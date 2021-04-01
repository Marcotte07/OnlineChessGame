-- get rid of line 2 when necessary
DROP DATABASE IF EXISTS chess;
CREATE DATABASE chess;
USE chess;

CREATE TABLE User (
	user_id INT PRIMARY KEY,
    username VARCHAR(30),
    password VARCHAR(30),
    firstname VARCHAR(30),
    lastname VARCHAR(30),
    timestampe DATETIME,
    elo INT,
    num_wins INT,
    num_losses INT,
    num_ties INT,
    num_games INT
);

CREATE TABLE Game (
	game_id INT PRIMARY KEY,
    white_player_id INT,
    black_player_id INT,
    FOREIGN KEY (white_player_id) REFERENCES User(user_id),
    FOREIGN KEY (black_player_id) REFERENCES User(user_id),
    -- can pass only those 3 strings, any other string will give an error
    game_status ENUM('win', 'loss', 'tie'),
    start_time DATETIME,
    end_time DATETIME,
    white_player_elo INT,
    black_player_elo INT
);