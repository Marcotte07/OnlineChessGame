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

			out.println("<table style=\"width:70%;margin: 0 auto; border-spacing: 10px;\">");
			out.println("<tr>");
			out.println("<th>Elo</th>");
			out.println("<th>Number of Wins</th>");
			out.println("<th>Number of Losses</th>");
			out.println("<th>Number of Ties</th>");
			out.println("</tr>");
			
			
			out.println("<tr style=\"background:lightgrey;color:black\">");
			out.println("<th style=\"border-radius:30px;\">"+u.elo+"</th>");
			out.println("<th style=\"border-radius:30px;\">"+u.numWins+"</th>");
			out.println("<th style=\"border-radius:30px;\">"+u.numLosses+"</th>");
			out.println("<th style=\"border-radius:30px;\">"+u.numTies+"</th>");
			out.println("</tr></table>");

			out.println("<br><br><br><h1 style=\"text-align:center;font-weight:bold\">Match History</h1><br>");
			
			
			out.println("<table style=\"width:60%;margin: 0 auto;\">");
			out.println("<tr style=\"background: black;\">");
			out.println("<th>W/L</th>");
			out.println("<th>Game</th>");
			out.println("<th>"+u.username+" Elo</th>");
			out.println("<th>Opponent Elo</th>");
			out.println("<th>Game Length</th>");
			out.println("<th>Date</th>");
			out.println("<th>Time</th>");
			out.println("</tr>");
			
			//yes im that lazy:
			int counter = 1;
			for(Game g : games) {
				if (counter % 2 == 0)
					out.println("<tr style=\"background:lightgrey;color:black\">");
				else 
					out.println("<tr style=\"background:darkgrey;color:black\">");
				++counter;
				String oppUsername = "";
				
				//If user was white player
				if(g.whitePlayerId == u.id) {
					User tmp = idIsKnown(g.blackPlayerId,knownUsers);
	
					if(tmp == null) {
						tmp = q.searchUser(g.blackPlayerId);	

					}
					
					if(tmp != null) {
						knownUsers.add(tmp);
						oppUsername = tmp.username;
					} else {
						oppUsername = "Guest";
					}
					
					
					//W/L
					if(g.gameStatus.toLowerCase().equals("win")) {
						out.println("<th>Win</th>");
					} else if (g.gameStatus.toLowerCase().equals("loss")){
						out.println("<th>Loss</th>");
					} else {
						out.println("<th>Tie</th>");
					}
					
					//Game
					if (tmp != null) {
						out.println("<th><a href=\"profile.html?name=" + username + 
								"\"><span style=\"color:green\">"+username+
								"</span></a> vs <a href=\"profile.html?name=" + 
								oppUsername + "\">"+oppUsername+"</a></th>");
					} else {
						out.println("<th><a href=\"profile.html?name=" + username + 
								"\"><span style=\"color:green\">"+username+
								"</span></a> vs "+oppUsername+"</th>");
					}
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
					
					if(tmp != null) {
						knownUsers.add(tmp);
						oppUsername = tmp.username;
					} else {
						oppUsername = "Guest";
					}
					
					//W/L
					if(g.gameStatus.toLowerCase().equals("loss")) {
						out.println("<th>Win</th>");
					} else if (g.gameStatus.toLowerCase().equals("win")){
						out.println("<th>Loss</th>");
					} else {
						out.println("<th>Tie</th>");
					}
					
					//Game
					if(tmp != null) {
						out.println("<th><a href=\"profile.html?name=" + oppUsername + "\">"
								+oppUsername+"</a> vs <a href=\"profile.html?name=" + username +
								"\"><span style=\"color:green\"> "+username+ "</a></span></th>");
					} else {
						out.println("<th> " +oppUsername+" vs <a href=\"profile.html?name=" + username +
								"\"><span style=\"color:green\"> "+username+ "</a></span></th>");
					}
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
	            out.println("<th>" + g.startTime + "</th>");
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