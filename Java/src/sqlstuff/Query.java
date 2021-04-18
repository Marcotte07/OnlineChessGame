package sqlstuff;

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
	
	Query(String ipAddress, int port) throws SQLException {
		conn = DriverManager.getConnection("jdbc:mysql://" + ipAddress + ":"+ port 
				+ "3306/chess?user=root&password=root");
	}
	
	Query() throws SQLException {
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
	
	public User searchUser(String username) {
		PreparedStatement ps = null;
		ResultSet rs = null;
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
	
	public void createAccount(
			String username, String password, String firstname, String lastname)
	{
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO \n"
					+ "user(username, password, firstname, lastname, date_created, elo, num_wins, num_losses,\n"
					+ "num_ties, num_games) \n"
					+ "VALUES\n"
					+ "(?, ?, ?, ?, ?, 1000, 0, 0, 0, 0)");
			
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, firstname);
			ps.setString(4, lastname);
			ps.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
		try {  q = new Query(); }
		catch (SQLException sqle) {
			
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
