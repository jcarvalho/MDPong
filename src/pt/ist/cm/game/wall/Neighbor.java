package pt.ist.cm.game.wall;

import pt.ist.cm.game.ColisionType;
import pt.ist.cm.game.ball.Vector;
import android.util.Log;

public class Neighbor extends Wall {

	private static final int DEFAULT_THICKNESS = 30;
	private boolean warning = false;

	public Neighbor(WallType type) {
		super(type, DEFAULT_THICKNESS);
		Log.d(this.getClass().getName(), "Neighbor ctor!");
	}

	@Override
	public ColisionType colidesWith(Vector position) {
		ColisionType type = collisionPredicate.evaluate(position);
		return type == ColisionType.NONE ? type : ColisionType.NEIGHBOR;
	}

	public ColisionType ballApproachingNeighbor(Vector position) {
		Vector neighborVec = new Vector();
		int incX, incY;
		int threshold = DEFAULT_THICKNESS;
		if (this.getType().equals(WallType.LEFTWALL)) {
			incX = -threshold;
			incY = 0;
		} else if (this.getType().equals(WallType.TOPWALL)) {
			incX = 0;
			incY = -threshold;
		} else if (this.getType().equals(WallType.RIGHTWALL)) {
			incX = threshold;
			incY = 0;
		} else { // WallType.BOTTOMWALL
			incX = 0;
			incY = threshold;
		}
		neighborVec.setX(position.getX() + incX);
		neighborVec.setY(position.getY() + incY);

		ColisionType type = collisionPredicate.evaluate(neighborVec);
		return type == ColisionType.NONE ? type : ColisionType.NEIGHBOR;
	}

	public boolean isCloserTo(Vector prevPos, Vector nextPos) {

		if (getType().equals(WallType.RIGHTWALL)) {
			Log.d(this.getClass().getName(), "RIGHTWALL: " + nextPos.getX()
					+ " " + prevPos.getX());
			return nextPos.getX() < prevPos.getX();
		} else if (getType().equals(WallType.LEFTWALL)) {
			Log.d(this.getClass().getName(), "LEFTWALL: " + nextPos.getX()
					+ " " + prevPos.getX());
			return nextPos.getX() > prevPos.getX();
		} else if (getType().equals(WallType.TOPWALL)) {
			Log.d(this.getClass().getName(), "TOPWALL: " + nextPos.getY() + " "
					+ prevPos.getY());
			return nextPos.getY() < prevPos.getY();
		} else {
			Log.d(this.getClass().getName(), "BOTTOMWALL: " + nextPos.getY()
					+ " " + prevPos.getY());
			return nextPos.getY() > prevPos.getY();
		}
	}

	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warn) {
		this.warning = warn;
	}
}
