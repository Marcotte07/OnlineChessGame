import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.Cookie;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
//
@ServerEndpoint(value = "/GameEndpoint")
public class ServerSocket {

	//private static Vector<Session> sessionVector = new Vector<Session>();
	// since open() is synchronized, queue of size 2 makes sense
	private static BlockingQueue<Session> sessionQueue = new LinkedBlockingQueue<Session>(2);
	private static Map<Session, Session> opponentSession = new ConcurrentHashMap<Session, Session>();
	private static Map<Session, String> cookieMap = new ConcurrentHashMap<Session, String>();
	private static Map<Session, String> hasEndedMap = new ConcurrentHashMap<Session, String>();
	//private static Map<Session, String> color = new ConcurrentHashMap<Session, String>();
	
	private Query q;
	
	public ServerSocket() throws IOException, ClassNotFoundException, SQLException {
		q = new Query();
	}
	
	
	// decided to make this synchronized because pretty much the entire function is critical section
	@OnOpen
	public synchronized void open(Session session) throws IOException {

		if (sessionQueue.size() > 0) {
			Session opponent = sessionQueue.remove();
			
			System.out.println(opponent.getRequestParameterMap());
			
			opponentSession.put(session, opponent);
			opponentSession.put(opponent, session);
			
			/* DOING THIS FOR SOME REASON GETS THE PIECES PREVIEW WRONG FOR WHITE
			if(Math.random() < 0.5) {
				session.getBasicRemote().sendText("w");
				opponent.getBasicRemote().sendText("b");
			}
			else {
				session.getBasicRemote().sendText("b");
				opponent.getBasicRemote().sendText("w");
			}
			
			*/
			session.getBasicRemote().sendText("b");
			opponent.getBasicRemote().sendText("w");
		}
		// no one else is playing :( ... have to wait in queue
		else {
			sessionQueue.add(session);
		}
	}
	////
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		
		if(message.toLowerCase().contains("gameover")) {
			System.out.println("GameOverMessage: " + message);
			String[] csv = message.split(",");
			String white = csv[1];
			String black = csv[2];
			String state = csv[3];
			
			q.updatePlayerGamesPlayed(white, black, state);
			q.updateElo(white, black);
			
			return;
		}
		
		//opponentSession.get(session).getBasicRemote().sendText(message);
		System.out.println(message);
		// will leave this for debugging purposes

		
		// The client is sending USERNAME, send back opponents username!
		if(message.substring(0, 9).equals("username=")) {
			cookieMap.put(session, message);
			System.out.println(cookieMap);
			
			Session opponent = opponentSession.get(session);
			
			// No opponent yet, current client still in queue
			if(opponent == null) {
				return;
			}
			
			String opponentUsername = cookieMap.get(opponent);
			String myUsername = cookieMap.get(session);
			// Now send opponents username to session that sent their username
			// Also send MY username to opponent :)
			session.getBasicRemote().sendText(opponentUsername);
			opponent.getBasicRemote().sendText(myUsername);
		}
		
		// The client is sending MOVE, sent to other client
		else {
			System.out.println("Sending move to other client");
			opponentSession.get(session).getBasicRemote().sendText(message);
			System.out.println(message);
		}
	}
	
	@OnClose
	public void close(Session session) throws IOException, SQLException {
		System.out.println("Removing session with username " + cookieMap.get(session));
		
		/*
		// If this session disconnected during a game, it should be an automatic loss
		if(opponentSession.get(session) != null) {
			
			Session opponent = opponentSession.get(session);
			
			opponent.getBasicRemote().sendText("YOU WON BY DEFAULT");
			
			
			
			// opponent won, so send message and then update their score
			System.out.println("opponent disconnected");
			
			
			opponentSession.remove(opponent);
		}
		
		*/
		// Check if they were in the queue and remove if so
		if(sessionQueue.contains(session)) {
			System.out.println("removing session from queue because disconnected before finding a match");
			sessionQueue.remove(session);
		}
		
		opponentSession.remove(session);
		
		System.out.println("onclose deallocate");
	//	if (q != null)
//			q.close();
	}
	
	@OnError
	public void error(Throwable error) throws SQLException {		
		error.printStackTrace();
		
		System.out.println("onerror deallocate");
	//	if (q != null)
		//	q.close();
	}
	

}