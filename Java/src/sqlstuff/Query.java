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

public class Query {

	private int numAttempts;
	
	
	Query(String ipAddress, int port) {
		// TODO: gonna need the port and ipAddress for the aws thing
		setNumAttempts(0);
	}
	
	Query() {
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
		
	public void autheticate(String username, String password)
	throws SQLInvalidAuthorizationSpecException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chess?user=root&password=root");
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
				if (conn != null) conn.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public static Vector<User> getTopPlayers(int threshold) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chess"
					+ "?user=root&password=root");
			ps = conn.prepareStatement("SELECT * FROM User ORDER BY elo");
			rs = ps.executeQuery();
			Vector<User> topPlayers = null;
			while (rs.next()) {
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
				if (conn != null) conn.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public static User searchUser(String username) 
			throws SQLInvalidAuthorizationSpecException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chess"
					+ "?user=root&password=root");
			ps = conn.prepareStatement("SELECT * FROM User "
					+ "WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next()) 
				throw new SQLInvalidAuthorizationSpecException("invalid username");
			
			// TODO: pass the proper arguments to constructor
			return new User();
			
		} catch(SQLInvalidAuthorizationSpecException iase) {
			throw iase;
		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
				if (conn != null) conn.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public static void createAccount(String email, String username, String password)
	{
		
	}
	
	public static void main(String args[]) {
		// le epic unit tests
		
		// authenticate
		Query q = new Query();
		
		try {
			q.autheticate("saskool", "poop");
			System.out.println("no exception");
		} catch (SQLInvalidAuthorizationSpecException sqle) {
			System.out.println("exception thrown: " + sqle);
		}
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
