package pt.ist.cm.game;

import pt.ist.cm.game.ball.Vector;

public interface ColisionPredicate {

	public ColisionType evaluate(Vector vector);

}
