import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Leaderboards")
public class Leaderboards extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private Query q;
	
	public Leaderboards() throws IOException, ClassNotFoundException, SQLException {
		q = new Query();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("Fetching leaderboards");
		Vector<User> users = null;
		
		users = q.getTopPlayers(10);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		out.println("<table style=\"width:70%;margin: 0 auto;\">");
		out.println("<tr style=\"background: black;\">");
		out.println("<th>Rank</th>");
		out.println("<th>Username</th>");
		out.println("<th>Elo</th>");
		out.println("<th>Wins</th>");
		out.println("<th>Losses</th>");
		out.println("<th>Ties</th>");
		out.println("</tr>");
		
		int place = 1;
		for(User u : users) {
			//<a href="url">link text</a>
			if (place % 2 == 0)
				out.println("<tr style=\"background: lightgrey; color:black\">");
			else 
				out.println("<tr style=\"background: darkgrey; color: black\">");
 
			out.println("<th>" + place + "</th>");
			out.println("<th><a style=\"color:" + (place%2==0?"black":"black")+ "\" href=\"profile.html?name="+u.username+"\">" + u.username + "</a></th>");
			out.println("<th>" + u.elo + "</th>");
			out.println("<th>" + u.numWins + "</th>");
			out.println("<th>" + u.numLosses + "</th>");
			out.println("<th>" + u.numTies + "</th>");
			out.println("</tr>");

			place++;
		}
		out.println("</table>");
		out.close();		
	}
	

}
