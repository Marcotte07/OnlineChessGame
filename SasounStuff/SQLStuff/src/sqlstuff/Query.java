package sqlstuff;

import java.lang.IllegalArgumentException;

// sql classes that i need
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
	private Connection conn;
	private Statement st;
	private int numAttempts;
	
	
	Query(String ipAddress, int port) {
		// TODO: gonna need the port and ipAddress for the aws thing
	}
	
	Query() {
		conn = null;
		st = null;
		numAttempts = 0;
		try {
			// i know, not a very secure password
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
					+ "studentgrades?user=root&password=root");
			st = conn.createStatement();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			try {
				if (st != null) st.close();
				if (conn != null) conn.close();
			// i dont know what else to name it, i hate java
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		} 
	}
	
	public int getNumAttempts() {
		return numAttempts;
	}
	
	public void setNumAttempts(int numAttempts) {
		if (numAttempts < 0)
			throw new IllegalArgumentException("Tried to set numAttempts to " + numAttempts);
		this.numAttempts = numAttempts;
	}
	
	static void sendEmail(String username) {
		// TODO: how the hell do i send an email?
	}
	
	static void autheticate(String username, String password) {
		// TODO: make a function called findUsername
	}
	
	
	public static void main(String args[]) {
		System.out.println("Sasoun is the coolest");
	}
}
