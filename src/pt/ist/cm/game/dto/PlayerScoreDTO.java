package pt.ist.cm.game.dto;

import pt.ist.cm.game.NetworkEventListener;

public class PlayerScoreDTO extends MDPongDTO {

	private final String playerId;
	private final String ballId;
	private final boolean won;

	public PlayerScoreDTO(String playerId, String ballId, boolean won) {
		super();
		this.playerId = playerId;
		this.ballId = ballId;
		this.won = won;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getBallId() {
		return ballId;
	}

	public boolean hasWon() {
		return won;
	}

	@Override
	public void visit(NetworkEventListener listener) {
		listener.playerScored(this);
	}

}
