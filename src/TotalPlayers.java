import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TotalPlayers")
public class TotalPlayers extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private Query q;
	
	public TotalPlayers() throws IOException, ClassNotFoundException, SQLException {
		q = new Query();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		  	  

		int totalPlayers = q.getTotalPlayers();

		out.print(totalPlayers);
		
      	out.flush();
      	out.close();
	}
	
	

}