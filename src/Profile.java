import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserProfile")
public class Profile extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("Fetching player data");
		System.out.println(request.getParameter("name"));
		
		User u = null;
		try {
			Query q = new Query();
			u = q.searchUser(request.getParameter("name"));
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			if(u == null) {
				out.print("none");
			} else {
				//out.println("<a href=\"profile.html?name="+u.username+"\">" + u.username + "</a><br>");
				out.println("Elo: " +u.elo+"<br>");
				out.println("num wins:" + u.numWins + "<br>");
				out.println("num losses:" + u.numLosses + "<br>");
				out.println("num ties: " + u.numTies + "<br>");
			}
				
			out.close();
			q.close();
			
		} catch(SQLException sqle) {
			System.out.println("Leaderboards sqle: " + sqle);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Class not found exceptoion in leaderboards: " + cnfe);
		}
		

		
		
	}
}