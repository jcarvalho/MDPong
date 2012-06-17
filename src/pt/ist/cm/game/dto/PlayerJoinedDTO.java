package pt.ist.cm.game.dto;

import pt.ist.cm.game.NetworkEventListener;

public class PlayerJoinedDTO extends MDPongDTO {

	private final String playerId, playerURL;

	public PlayerJoinedDTO(String playerId, String playerURL) {
		this.playerId = playerId;
		this.playerURL = playerURL;
	}

	@Override
	public void visit(NetworkEventListener listener) {
		listener.playerJoined(this);
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getPlayerURL() {
		return playerURL;
	}

}
