package pt.ist.cm.game.dto;

import pt.ist.cm.game.NetworkEventListener;
import pt.ist.cm.game.ball.Ball;

public class BallControlDTO extends MDPongDTO {

	private final String id;

	private final String playerID;

	private final boolean toDelete;

	public BallControlDTO(Ball ball, String playerId, boolean toDelete) {
		this.id = ball.getId();
		this.playerID = playerId;
		this.toDelete = toDelete;
	}

	public BallControlDTO(String ballId, String playerId, boolean toDelete) {
		this.id = ballId;
		this.playerID = playerId;
		this.toDelete = toDelete;
	}

	public String getId() {
		return id;
	}

	public String getPlayer() {
		return playerID;
	}

	public boolean toDelete() {
		return toDelete;
	}

	@Override
	public void visit(NetworkEventListener listener) {

		// if (BallManager.ballExists(id))
		// listener.ballMoved(this);
		// else
		// listener.newBall(this);
		listener.ballControl(this);
	}
}
