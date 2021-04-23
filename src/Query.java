
import java.util.Vector;

// sql classes that i need
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;

// time stuff
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/*
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 */
public class Query {
	private Connection conn;
	
	// DO NOT FORGE TO CALL .CLOSE()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void close() throws SQLException {
		if (conn != null)
			conn.close();
	}
	
	Query(String ipAddress, int port, String user, String password) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + ipAddress + ":"+ port 
				+ "/chess?user=" + user + "&password=" + password);
	}
	
	Query() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chess"
				+ "?user=root&password=root");
	}
	
	// i am on the fence about declaring this as void.... should be boolean but im not sure
	public boolean autheticate(String username, String password) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT username, password FROM User "
					+ "WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next()) 
				return false;
			// check invalid password
			if (!rs.getString("password").equals(password)) 
				return false;
			
			return true;
		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public Vector<User> getTopPlayers(int threshold) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM User ORDER BY elo DESC");
			rs = ps.executeQuery();
			Vector<User> topPlayers = new Vector<User>();
			while (rs.next()) {
				if (topPlayers.size() >= threshold) 
					break;
			
				topPlayers.add(
						new User(
								rs.getInt("user_id"),
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("date_created"),
								rs.getInt("elo"),
								rs.getInt("num_wins"),
								rs.getInt("num_losses"),
								rs.getInt("num_ties"),
								rs.getInt("num_games")
								)
						);
			}
			return topPlayers;
		} catch (ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	
	public Vector<Game> getPlayerGames(String username) {
		
			Vector<Game> games = new Vector<Game>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			User player = searchUser(username);
			int id = player.id;
			try {
				//TODO: Finish get player games function
				String query = "SELECT * FROM game a WHERE white_player_id=? or black_player_id=? ORDER BY game_id DESC";
				ps = conn.prepareStatement(query);
				ps.setInt(1, id);
				ps.setInt(2, id);
				rs = ps.executeQuery();
				
				while(rs.next()) {
					games.add(new Game(
							rs.getInt("game_id"), 
							rs.getInt("white_player_id"), 
							rs.getInt("black_player_id"), 
							rs.getString("game_status"), 
							rs.getTime("start_time"), 
							rs.getTime("end_time"), 
							rs.getDate("start_time"), 
							rs.getDate("end_time"), 
							rs.getInt("white_player_elo"), 
							rs.getInt("black_player_elo")));
				}
				
				if(rs != null) 
					rs.close();
				if(ps != null)
					ps.close();
			} catch (SQLException sqle) {
				System.out.println("SQLE: " + sqle);
			}

			return games;
	}
	
	
	public User searchUser(String key, String value) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			System.out.println("SELECT * FROM User n WHERE " +key + "="+ value);
			
			ps = conn.prepareStatement("SELECT * FROM User "
					+ "WHERE ?=?");
			ps.setString(1, key);
			ps.setString(2, value);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next())  {
				return null;
			}
			
			return new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("firstname"),
					rs.getString("lastname"),
					rs.getString("date_created"),
					rs.getInt("elo"),
					rs.getInt("num_wins"),
					rs.getInt("num_losses"),
					rs.getInt("num_ties"),
					rs.getInt("num_games")
					);
			
		} catch(ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	public User searchUser(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			System.out.println("SELECT * FROM User n WHERE user_id="+ id);
			
			ps = conn.prepareStatement("SELECT * FROM User "
					+ "WHERE user_id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next())  {
				return null;
			}
				
			System.out.print("returning user");
			
			return new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("firstname"),
					rs.getString("lastname"),
					rs.getString("date_created"),
					rs.getInt("elo"),
					rs.getInt("num_wins"),
					rs.getInt("num_losses"),
					rs.getInt("num_ties"),
					rs.getInt("num_games")
					);
			
		} catch(ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public User searchUser(String username) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if(username.toLowerCase().equals("userisguest")) {
			return null;
		}
		
		try {
			ps = conn.prepareStatement("SELECT * FROM User "
					+ "WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next()) 
				return null;
			
			return new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("firstname"),
					rs.getString("lastname"),
					rs.getString("date_created"),
					
					rs.getInt("elo"),
					rs.getInt("num_wins"),
					rs.getInt("num_losses"),
					rs.getInt("num_ties"),
					rs.getInt("num_games")
					);
		} catch(ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public void updatePlayerGamesPlayed(String whitePlayerUsername, String blackPlayerUsername, String whiteGameState) {
		
		User white = searchUser(whitePlayerUsername);
		User black = searchUser(blackPlayerUsername);
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			
			if(whiteGameState.toLowerCase().equals("win")) {
				
				if(white != null) {
					ps = conn.prepareStatement("UPDATE user SET num_wins=num_wins+1, num_games=num_games+1 WHERE user_id=?;");
					ps.setInt(1, white.id);
					ps.executeUpdate();
				}
				
				if(black != null) {
					ps2 = conn.prepareStatement("UPDATE user SET num_losses=num_losses+1, num_games=num_games+1 WHERE user_id=?;");
					ps2.setInt(1, black.id);
					ps2.executeUpdate();
				}
				

			} else if (whiteGameState.toLowerCase().equals("loss")) {
				if(black != null) {
					ps = conn.prepareStatement("UPDATE user SET num_wins=num_wins+1, num_games=num_games+1 WHERE user_id=?;");
					ps.setInt(1, black.id);
					ps.executeUpdate();
				}
				
				if(white != null ) {
					ps2 = conn.prepareStatement("UPDATE user SET num_losses=num_losses+1, num_games=num_games+1 WHERE user_id=?;");
					ps2.setInt(1, white.id);
					ps2.executeUpdate();
				}
			} else {
				if(black != null) {
					ps = conn.prepareStatement("UPDATE user SET num_ties=num_ties+1, num_games=num_games+1 WHERE user_id=?;");
					ps.setInt(1, black.id);
					ps.executeUpdate();
				}
		
				if(white != null) {
					ps2 = conn.prepareStatement("UPDATE user SET num_ties=num_ties+1, num_games=num_games+1 WHERE user_id=?;");
					ps2.setInt(1, white.id);
					ps2.executeUpdate();
				}
			
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				if (ps != null) ps.close();
				if (ps2 != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public void updateElo(String winningPlayersUsername, String LosingPlayersUsername) {
		System.out.println("Update Elo");
		
		User winner = searchUser(winningPlayersUsername);
		User loser = searchUser(LosingPlayersUsername);
		
		int wElo = winner.elo;
		int lElo = loser.elo;
		
		Double winningProbability =  1.0 / (1.0 + Math.pow(10.0,(lElo*1.0 - wElo*1.0)/400.0));
		//Double losingProbability = 1.0 / (1.0 + Math.pow(10.0,(wElo*1.0 - lElo*1.0)/400.0));
		
		Double multiplier = 100.0;

        int winningPlayerNewElo = (int)(multiplier * (1-winningProbability));
        int losingPlayerNewElo = (int)(multiplier * (1-winningProbability)*-1.0);
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			
			if(winner != null) {
				ps = conn.prepareStatement("UPDATE user SET elo=elo+? WHERE user_id=?;");
				ps.setInt(1, winningPlayerNewElo);
				ps.setInt(2, winner.id);
				ps.executeUpdate();
			}
			
			if(loser != null) {
				ps2 = conn.prepareStatement("UPDATE user SET elo=elo+? WHERE user_id=?;");
				ps2.setInt(1, losingPlayerNewElo);
				ps2.setInt(3, loser.id);
				ps2.executeUpdate();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				if (ps != null) ps.close();
				if (ps2 != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
		
        
	}
	public void createAccount(String username, String email, String password, 
							  String firstname, String lastname)
	{
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO \n"
					+ "user(username, email, password, firstname, lastname, date_created, elo, num_wins, num_losses,\n"
					+ "num_ties, num_games) \n"
					+ "VALUES\n"
					+ "(?, ?, ?, ?, ?, ?, 1000, 0, 0, 0, 0)");
			
			ps.setString(1, username);
			ps.setString(2, email);
			ps.setString(3, password);
			ps.setString(4, firstname);
			ps.setString(5, lastname);
			ps.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			ps.executeUpdate();

		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
			//return null;
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		// le epic unit tests
		
		// authenticate
		Query q = null;
		try {  
			q = new Query(
					"database-1.ct0zvlj1qp3l.us-east-2.rds.amazonaws.com",
					3306,
					"admin",
					"sasouniscool"
					); 
			q.autheticate("test", "test");

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe){
			
		}
		
		// getTopPlayers works!
		Vector<User> v = q.getTopPlayers(100);
		for (int i = 0; i < v.size(); ++i) {
			System.out.println(v.get(i).elo);
		}
		
		// NOTE: if you run this line of code twice, you will get a runtime error because I made each 'username' unique!
		// q.createAccount("user", "pass", "first", "last");
	}
}

/*
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * 
 */
