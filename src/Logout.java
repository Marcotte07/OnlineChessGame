import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Logout")
public class Logout extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();

  	  	boolean found = false;

  	  
		if( cookies != null ) {
		 
		    for (int i = 0; i < cookies.length; i++) {
		       cookie = cookies[i];
		       if(cookie.getName().equals("userid")) {
		    	   found = true;
		      	 	cookie.setMaxAge(0);
		        	 response.addCookie(cookie);
		         }
		      }
		   } 

      if(!found) {
    	  out.print("none");
    	  System.out.println("Logout: None");
      } else {
    	  System.out.println("Logout: Success");
    	  out.print("success");
      }
      out.close();
	}
}