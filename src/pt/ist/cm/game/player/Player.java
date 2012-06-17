package pt.ist.cm.game.player;

import pt.ist.cm.game.GameConstants;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;

public class Player {

	private final String playerId;
	private int score = GameConstants.INITIAL_SCORE;

	public Player(String playerId) {
		this.playerId = playerId;
		NotificationManager.notifyListeners(EventType.PLAYER_JOINED,
				Player.class, this);
	}

	public void score() {
		if (score > 0) {
			if (--score == 0)
				NotificationManager.notifyListeners(EventType.PLAYER_LOST,
						Player.class, this);
			else
				NotificationManager.notifyListeners(EventType.PLAYER_SCORED,
						Player.class, this);
		}
	}

	public int getScore() {
		return score;
	}

	public String getPlayerId() {
		return playerId;
	}

	public boolean hasLost() {
		return score == 0;
	}

	@Override
	public boolean equals(Object aThat) {
		// check for self-comparison
		if (this == aThat)
			return true;

		if (!(aThat instanceof Player))
			return false;

		Player that = (Player) aThat;

		// now a proper field-by-field evaluation can be made
		return this.playerId.equals(that.playerId);
	}

}
