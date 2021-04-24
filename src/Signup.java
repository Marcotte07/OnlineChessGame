import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Signup")
public class Signup extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("signup");
		String uname = request.getParameter("sign_uname");
		String pwd = request.getParameter("subm_pwd");
		String email = request.getParameter("sign_email");
		String fName = request.getParameter("first_name");
		String lName = request.getParameter("last_name");
		
		try {
			Query q = new Query();
			
			User uEmail = q.searchUser("email",email);
			User uUname = q.searchUser(uname);
			
			
			System.out.println("Signup Username id: " + uUname);
			System.out.println("Signup Email id: " + uEmail);
			
			PrintWriter out = response.getWriter();
			
			if (uUname != null) {
				out.print("username_taken");
			} else if(uEmail != null) {
				out.print("email_taken");
			} else {
				q.createAccount(uname, email, pwd, fName, lName);
				out.print("valid");
			}
			q.close();
		} catch (ClassNotFoundException cnfe) {
			System.out.println("CNFE: "+cnfe);
		} catch (SQLException sqle) {
			System.out.println("SQLE: " + sqle);
		}
		
	}
	
}