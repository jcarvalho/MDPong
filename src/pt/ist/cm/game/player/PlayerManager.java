package pt.ist.cm.game.player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

	public static final String MASTER_PLAYER_ID = "0";

	private static final String NO_ID = "NO_ID";

	private static Map<String, Player> players = new HashMap<String, Player>();

	public static String playerId = NO_ID;

	public static int lastId = 1;

	private static boolean isMaster = false;

	public static String[] getAllScores() {
		String scores[] = new String[players.size()];
		int i = 0;
		for (Player player : players.values()) {
			int score = player.getScore();
			scores[i++] = "Player #" + player.getPlayerId() + ": "
					+ (score == 0 ? "KO" : score);
		}
		return scores;
	}

	public static int getNumPlayers() {
		return players.entrySet().size();
	}

	public synchronized static String getNewPlayerId() {
		return (++lastId) + "";
	}

	public static void addNewPlayer(String playerId) {

		Player player = new Player(playerId);

		players.put(playerId, player);
	}

	public static Player getPlayerById(String playerId) {
		return players.get(playerId);
	}

	public static void setPlayerId(String id) {
		playerId = id;
	}

	public static void setThisAsMaster() {
		addNewPlayer(MASTER_PLAYER_ID);
		playerId = MASTER_PLAYER_ID;
		isMaster = true;
	}

	public static boolean isMasterPlayer() {
		return isMaster;
	}

	public static void clear() {
		players.clear();
		playerId = NO_ID;
		lastId = 0;
		isMaster = false;
	}

	public static Player getCurrentPlayer() {
		return players.get(playerId);
	}

	public static boolean haveIWon() {
		if (players.size() == 1)
			return false;
		for (Player p : players.values()) {
			if (p.getPlayerId().equals(playerId))
				continue;
			if (p.getScore() > 0)
				return false;
		}

		return true;
	}

	public static void removePlayer(String playerId) {
		players.remove(playerId);
		lastId--;
	}
}
