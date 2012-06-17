package pt.ist.cm.game.dto;

import pt.ist.cm.game.NetworkEventListener;

public class PlayerLeftDTO extends MDPongDTO {

	private final String playerId;

	public PlayerLeftDTO(String playerId) {
		super();
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}

	@Override
	public void visit(NetworkEventListener listener) {
		listener.playerLeft(this);
	}

}
