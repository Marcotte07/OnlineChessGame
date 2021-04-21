import java.util.*;

public class Game {

	public int gameId;
	public int whitePlayerId;
	public int blackPlayerId;
	public String gameStatus;
	public Date startTime;
	public Date endTime;
	public Date startDate;
	public Date endDate;
	public int whitePlayerElo;
	public int blackPlayerElo;
	
	public Game(
			int gameId,
			int whitePlayerId,
			int blackPlayerId,
			String gameStatus,
			Date startTime,
			Date endTime,
			Date startDate,
			Date endDate,
			int whitePlayerElo,
			int blackPlayerElo
			) {
		this.gameId = gameId;
		this.whitePlayerElo = whitePlayerElo;
		this.blackPlayerElo = blackPlayerElo;
		this.whitePlayerId = whitePlayerId;
		this.blackPlayerId = blackPlayerId;
		this.gameStatus = gameStatus;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startDate = startDate;
		this.endDate = endDate;
		
	}
}
