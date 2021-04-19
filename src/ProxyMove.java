import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/*
NOTEE: I THOUGHT ABOUT THIS AND REALIZED WHAT NICK SAID WAS RIGHT, WE NEED TO USE WEB SOCKETS FOR
COMMUNICATION BETWEEN CLIENTS AND SERVER, I WILL TRY MY BEST TO UNDERSTAND
*/







@WebServlet("/ProxyMove")
public class ProxyMove extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		String position = request.getParameter("position");
		String newPosition = request.getParameter("newPosition");
		
		
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		out.println("{\"position\":\"" + position + "\",\"newPosition\":\"" + newPosition + "\"}");
	}
	

}
