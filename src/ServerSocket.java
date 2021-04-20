import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
//
@ServerEndpoint(value = "/ws")
public class ServerSocket {

	//private static Vector<Session> sessionVector = new Vector<Session>();
	// since open() is synchronized, queue of size 2 makes sense
	private static BlockingQueue<Session> sessionQueue = new LinkedBlockingQueue<Session>(2);
	private static Map<Session, Session> opponentSession = new ConcurrentHashMap<Session, Session>();
	//private static Map<Session, String> color = new ConcurrentHashMap<Session, String>();
	
	// decided to make this synchronized because pretty much the entire function is critical section
	@OnOpen
	public synchronized void open(Session session) throws IOException {
		System.out.println(" ddddmade!");
	//	sessionVector.add(session);
		// if another player is waiting in the queue, start a game
		///session.getBasicRemote().sendText("text");

		if (sessionQueue.size() > 0) {
			Session opponent = sessionQueue.remove();
			opponentSession.put(session, opponent);
			opponentSession.put(opponent, session);
			
		//	color.put(session, "w");
	//		color.put(opponent, "b");
			
			session.getBasicRemote().sendText("w");
			opponent.getBasicRemote().sendText("b");
		//	
			
		}
		// no one else is playing :( ... have to wait in queue
		else {
			sessionQueue.add(session);
		}
	}
	////
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
//		for (int i = 0; i < 100; ++i) {
//			opponentSession.get(session).getBasicRemote().sendText("{\"test\":\"num\"}");
	//	}
		//message = "{\"test\":\"test\"}";
		// send the opponent a json file of the move
		opponentSession.get(session).getBasicRemote().sendText(message);
		System.out.println(message);
		// will leave this for debugging purposes
		
		
	//	System.out.println(message);
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("sdfsdfsdfsdfsdfsdfsdfsdfsdf!");
		//sessionVector.remove(session);
	}
	
	@OnError
	public void error(Throwable error) {
		error.printStackTrace();
	}
	

}
