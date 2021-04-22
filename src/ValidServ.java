//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Properties;
//import java.util.Random;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//@WebServlet("/ValidServ")
//public class ValidServ extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		PrintWriter out = response.getWriter();
//		String email = request.getParameter("email");
//		String user = request.getParameter("user");
//		String pass = request.getParameter("pass");
//		String to = email;
//		String from = "chessexampleusc@gmail.com";
//		Properties properties = new Properties();
//		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
//		properties.put("mail.smtp.host", "smtp.gmail.com");    
//		properties.put("mail.smtp.socketFactory.port", "465");    
//		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");    
//		properties.put("mail.smtp.auth", "true");    
//		properties.put("mail.smtp.port", "465");
//		Session session = Session.getDefaultInstance(properties,
//		        new javax.mail.Authenticator() {    
//		        protected PasswordAuthentication getPasswordAuthentication() {    
//		        return new PasswordAuthentication(from, "DevilMayCry5");  // change to new pw
//		        }    
//		       });    
//				
//		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
//		String specialCharacters = "!@#$";
//		String numbers = "1234567890";
//		String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
//		Random random = new Random();
//		int length = 10;
//		String password = "";
//
//		password += lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
//		password += capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
//		password += specialCharacters.charAt(random.nextInt(specialCharacters.length()));
//		password += numbers.charAt(random.nextInt(numbers.length()));
//
//		for(int i = 4; i< length ; i++) {
//		   password += combinedChars.charAt(random.nextInt(combinedChars.length()));
//		}
//
//		try {
//		   // Create a default MimeMessage object.
//		   MimeMessage message = new MimeMessage(session);
//		   
//		   // Set From: header field of the header.
//		   message.setFrom(new InternetAddress(from));
//		   
//		   // Set To: header field of the header.
//		   message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//		   
//		   // Set Subject: header field
//		   message.setSubject("Email Verification Code");
//		   
//		   // Now set the actual message
//		   message.setText("Your verification code is "+password);
//		   
//		   // Send message
//		   Transport.send(message);
//		} catch (MessagingException mex) {
//		   mex.printStackTrace();
//		}		
//		
//		// Set response content type
//		response.setContentType("application/json");
//		out.println("{");
//		out.println("\"passcode\":\""+password+"\"");
//		out.println("}");
//		out.flush();
//		out.close();
//		
//		
//	}
//}
