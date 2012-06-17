package pt.ist.cm.game.dto;

import pt.ist.cm.game.NetworkEventListener;

public class PlayerJoinRequest extends MDPongDTO {

	private String playerIp;
	private final String port;

	public PlayerJoinRequest(String port) {
		this.port = port;
	}

	@Override
	public void visit(NetworkEventListener listener) {
		listener.playerJoinRequest(this);
	}

	public String getPlayerIp() {
		return playerIp;
	}

	public String getPort() {
		return port;
	}

	public void setPlayerIp(String playerIp) {
		this.playerIp = playerIp;
	}

}
