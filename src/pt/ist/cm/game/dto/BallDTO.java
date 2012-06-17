package pt.ist.cm.game.dto;

import pt.ist.cm.game.NetworkEventListener;
import pt.ist.cm.game.ball.Ball;
import pt.ist.cm.game.ball.BallManager;
import pt.ist.cm.game.ball.Vector;

public class BallDTO extends MDPongDTO {

	private final String id;

	private final Vector position;

	private final Vector direction;
	
	public BallDTO(Ball ball) {
		this.id = ball.getId();
		this.position = ball.getPosition();
		this.direction = ball.getDirection();
	}

	public String getId() {
		return id;
	}

	public Vector getPosition() {
		return position;
	}

	public Vector getDirection() {
		return direction;
	}

	@Override
	public void visit(NetworkEventListener listener) {

		if (BallManager.ballExists(id))
			listener.ballMoved(this);
		else
			listener.newBall(this);
	}
}
