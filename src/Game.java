
public class Game {

	public String gameId;
	public String whitePlayerId;
	public String blackPlayerId;
	public String gameStatus;
	public String startTime;
	public String endTime;
	public String whitePlayerElo;
	public String blackPlayerElo;
	
	public Game(
			String gameId,
			String whitePlayerId,
			String blackPlayerId,
			String gameStatus,
			String startTime,
			String endTime,
			String whitePlayerElo,
			String blackPlayerElo
			) {
		
		this.gameId = gameId;
		this.whitePlayerElo = whitePlayerElo;
		this.blackPlayerElo = blackPlayerElo;
		this.whitePlayerId = whitePlayerId;
		this.blackPlayerId = blackPlayerId;
		this.gameStatus = gameStatus;
		this.startTime = startTime;
		this.endTime = endTime;
		
	}
}
