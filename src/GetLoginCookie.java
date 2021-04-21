import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetLoginCookie")
public class GetLoginCookie extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("getlogincookies");
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
  	  	boolean found = false;
  	  	String value = "";
  	  
  	  	if( cookies != null ) {
  	  		System.out.println("cookies num: " + cookies.length);
	  	  	for (int i = 0; i < cookies.length; i++) {
	  	  		cookie = cookies[i];
	  	  		if(cookie.getName().equals("userid")) {
	  	  			found = true;
	  	  			value = cookie.getValue();
	  	  			System.out.println("cookies val: " + cookie.getValue());
	  	  		}
	  	  	}
  	  	} 

      	if(!found) {
      		out.print("none");
      		System.out.println("no cookies found");
  		} else {
  			out.print(value);
		}
      	out.flush();
      	out.close();
	}
}