package sqlstuff;

import java.util.Vector;
import java.lang.IllegalArgumentException;

// sql classes that i need
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;

// time stuff
import java.text.SimpleDateFormat;
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
	private int numAttempts;
	private Connection conn;
	
	// DO NOT FORGE TO CALL .CLOSE()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void close() throws SQLException {
		if (conn != null)
			conn.close();
	}
	
	Query(String ipAddress, int port) throws SQLException {
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chess?user=root&password=root");
		setNumAttempts(0);
	}
	
	Query() throws SQLException {
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chess?user=root&password=root");
		setNumAttempts(0);
	}
	
	public int getNumAttempts() {
		return numAttempts;
	}
	
	public void setNumAttempts(int numAttempts) {
		if (numAttempts < 0)
			throw new IllegalArgumentException("Tried to set numAttempts to " + numAttempts);
		this.numAttempts = numAttempts;
	}
	
	// i am on the fence about declaring this as void.... should be boolean but im not sure
	public void autheticate(String username, String password)
	throws SQLInvalidAuthorizationSpecException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT username, password FROM User "
					+ "WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next()) 
				throw new SQLInvalidAuthorizationSpecException("invalid username");
			// check invalid password
			if (!rs.getString("password").equals(password)) 
				throw new SQLInvalidAuthorizationSpecException("invalid password");
			
		} catch(SQLInvalidAuthorizationSpecException iase) {
			++numAttempts;
			throw iase;
		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
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
			ps = conn.prepareStatement("SELECT * FROM User ORDER BY elo");
			rs = ps.executeQuery();
			Vector<User> topPlayers = new Vector<User>();
			while (rs.next()) {
				if (topPlayers.size() >= threshold) 
					break;
				// TODO: properly pass arguments
				topPlayers.add(new User());
			}
			return topPlayers;
			
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
			
			// TODO: pass the proper arguments to constructor
			return new User();
			
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
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
		    Date date = new Date();  

			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, firstname);
			ps.setString(4, lastname);
			ps.setString(5, formatter.format(date));
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
		
		try {
			q.autheticate("saskool", "poop");
			System.out.println("no exception");
		} catch (SQLInvalidAuthorizationSpecException sqle) {
			System.out.println("exception thrown");
		}
		
		// need to test q.getTopPlayers
		Vector<User> v = q.getTopPlayers(3);
		System.out.println();
		
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
