INSERT INTO 
user(username, password, firstname, lastname, date_created, elo, num_wins, num_losses,
num_ties, num_games) 
VALUES
('saskool', 'poop', 'sas', 'kri', '2015/02/28 12:34:56', 1000, 202, 50, 6, 258),
('grapeDestroyer', 'I_HATE_GRAPES', 'grape', 'destroyer', '1111/11/11 11:11:11', 1111, 111, 11, 1, 1233),
('farmer', 'pitchfork', 'John', 'Smith', '2012/03/19 12:34:56', 500, 2, 30, 1, 33);

SELECT * FROM chess.user;
