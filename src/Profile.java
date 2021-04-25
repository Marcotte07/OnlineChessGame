import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserProfile")
public class Profile extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private Query q;
	
	public Profile() throws IOException, ClassNotFoundException, SQLException {
		q = new Query();
	}

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		String username = request.getParameter("name");
		System.out.println("Fetching player data");
		System.out.println(username);
		
		User u = null;
		ArrayList<User> knownUsers = new ArrayList<User>();
		
		u = q.searchUser(username);
		knownUsers.add(u);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		if(u == null) {
			out.print("none");
		} else {
			Vector<Game> games =  q.getPlayerGames(username);
			//out.println("<a href=\"profile.html?name="+u.username+"\">" + u.username + "</a><br>");
			out.println("Elo: " +u.elo+"<br>");
			out.println("num wins:" + u.numWins + "<br>");
			out.println("num losses:" + u.numLosses + "<br>");
			out.println("num ties: " + u.numTies + "<br>");
			
			out.println("<br>Recent Games: <br><br>");
			
			out.println("<table style=\"width:50%\">");
			out.println("<tr>");
			out.println("<th>W/L</th>");
			out.println("<th>Game</th>");
			out.println("<th>"+u.username+" Elo</th>");
			out.println("<th>Opponent Elo</th>");
			out.println("<th>Game Time</th>");
			out.println("<th>Date</th>");
			out.println("</tr>");
			
			for(Game g : games) {
				out.println("<tr>");
				
				String oppUsername = "";
				
				//If user was white player
				if(g.whitePlayerId == u.id) {
					User tmp = idIsKnown(g.blackPlayerId,knownUsers);
	
					if(tmp == null) {
						tmp = q.searchUser(g.blackPlayerId);	
						knownUsers.add(tmp);
					}
					
					oppUsername = tmp.username;
					
					//W/L
					if(g.gameStatus.toLowerCase().equals("win")) {
						out.println("<th>Win</th>");
					} else if (g.gameStatus.toLowerCase().equals("loss")){
						out.println("<th>Loss</th>");
					} else {
						out.println("<th>Tie</th>");
					}
					
					//Game
					out.println("<th><a href=\"profile.html?name=" + username + "\"><span style=\"color:green\">"+username+"</span></a> vs <a href=\"profile.html?name=" + oppUsername + "\">"+oppUsername+"</a></th>");
					
					//Player Elo
					out.println("<th>" + g.whitePlayerElo + "</th>");
					//Opponent Elo
					out.println("<th>" + g.blackPlayerElo + "</th>");
				
				//If user was black player
				} else {
					
					User tmp = idIsKnown(g.whitePlayerId,knownUsers);
					
					if(tmp == null) {
						tmp = q.searchUser(g.whitePlayerId);	
						knownUsers.add(tmp);
					}
					
					oppUsername = tmp.username;
					
					//W/L
					if(g.gameStatus.toLowerCase().equals("loss")) {
						out.println("<th>Win</th>");
					} else if (g.gameStatus.toLowerCase().equals("win")){
						out.println("<th>Loss</th>");
					} else {
						out.println("<th>Tie</th>");
					}
					
					//Game
					out.println("<th><a href=\"profile.html?name=" + oppUsername + "\">"+oppUsername+"</a> vs <a href=\"profile.html?name=" + username + "\"><span style=\"color:green\"> "+username+ "</a></span></th>");
					
					//Player Elo
					out.println("<th>" + g.blackPlayerElo + "</th>");
					//Opponent Elo
					out.println("<th>" + g.whitePlayerElo + "</th>");
				}
				
				SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd"); 
				SimpleDateFormat time_formatter = new SimpleDateFormat("mm:ss"); 
				Date start = g.startTime;
				Date end = g.endTime;
				
				long diff_time = end.getTime()-start.getTime();
				
	            
	            Date tmp = new Date(diff_time);
				//Game time
	            out.println("<th>" + time_formatter.format(diff_time) + "</th>");
				
				//Date
	            out.println("<th>" + g.startDate + "</th>");
				out.println("</tr>");
			}
		}
			
		out.flush();
		out.close();

	}
	
	
	private User idIsKnown(int id, ArrayList<User> knownUsers) {
		for(User u: knownUsers) {
			if(id == u.id) {
				return u;
			}
		}
		return null;
	}
	

}