import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Deallocate")
public class Deallocate extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private Query q;
	
	public Deallocate() throws IOException, ClassNotFoundException, SQLException {
		q = new Query();
	}

	public void destroy() {
		try {
			if (q != null)
				q.close();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}