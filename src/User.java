
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

// used public members because writing boilerplate is tedious and unnecessary
public class User {
	// credentials
	public int id;
	public String username;
	public String password;
	public String firstname;
	public String lastname;
	public Date dateCreated;
	
	// game info
	public int elo;
	public int numWins;
	public int numLosses;
	public int numTies;
	public int numGames;
	
	public User(
			int id,
			String username,
			String password,
			String firstname,
			String lastname,
			Date dateCreated,
			
			int elo,
			int numWins,
			int numLosses,
			int numTies,
			int numGames) {
		this.id = id;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.dateCreated = dateCreated;
		
		this.elo = elo;
		this.numWins = numWins;
		this.numLosses = numLosses;
		this.numTies = numTies;
		this.numGames = numGames;		
	}
	
	public User(
			int id,
			String username,
			String password,
			String firstname,
			String lastname,
			String dateCreated_str,
			
			int elo,
			int numWins,
			int numLosses,
			int numTies,
			int numGames) throws ParseException {
		// parse dateCreated_str
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date dateCreated = format.parse(dateCreated_str);
		
		this.id = id;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		
		this.dateCreated = dateCreated;
		this.elo = elo;
		this.numWins = numWins;
		this.numLosses = numLosses;
		this.numTies = numTies;
		this.numGames = numGames;		
	}
	
}
