package pt.ist.cm.game.wall;

import pt.ist.cm.game.ColisionType;
import pt.ist.cm.game.ball.Vector;

public class Pit extends Wall {

	public Pit(WallType type) {
		super(type);
	}

	@Override
	public ColisionType colidesWith(Vector position) {
		ColisionType type = collisionPredicate.evaluate(position);
		return type == ColisionType.NONE ? type : ColisionType.PIT;
	}
}
