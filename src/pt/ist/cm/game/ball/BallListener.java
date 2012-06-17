package pt.ist.cm.game.ball;

import pt.ist.cm.util.events.Listener;

public interface BallListener extends Listener {

	public void ballFell(Ball ball);

	public void ballEntry(Ball ball);

	public void ballEntering(Ball event);

	public void ballCreated(Ball ball);

}
