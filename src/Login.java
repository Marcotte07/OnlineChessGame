import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Login")
public class Login extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private Query q;
	
	public Login() throws IOException, ClassNotFoundException, SQLException {
		q = new Query();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Login called");
		
		User user = q.searchUser(request.getParameter("uname"));
		PrintWriter out = response.getWriter();
		
		System.out.println("user: " + request.getParameter("uname") + " || pw: " + request.getParameter("pwd"));
		if(user != null && user.password.equals(request.getParameter("pwd"))) {
			int id = user.id;
			
			Cookie cookie = new Cookie("userid", Integer.toString(id));
			cookie.setMaxAge(60 * 60 * 2); // (seconds * minutes * hours)
			response.addCookie(cookie);
			response.setContentType("text/html");
			out.print("valid");
			
		}else {
			System.out.println("Inavlid");
			out.print("invalid");
		}
		
		out.flush();
		out.close();
		
	}
	
	public void destroy() {
		try {
			q.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
}


