package pt.ist.cm.game.wall;

import pt.ist.cm.game.ColisionDetector;
import pt.ist.cm.game.ColisionPredicate;
import pt.ist.cm.game.ColisionType;
import pt.ist.cm.game.GameActivity;
import pt.ist.cm.game.ball.Vector;

public class Wall {

	public static enum WallType {
		LEFTWALL, RIGHTWALL, BOTTOMWALL, TOPWALL
	};

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	private final WallType type;

	protected final ColisionPredicate collisionPredicate;
	protected int thickness = 2;

	public Wall(WallType type) {
		this.type = type;
		setupCoords();
		WallManager.registerWall(this);
		collisionPredicate = ColisionDetector.getPredicateForRect(x, y, height,
				width);
	}

	public Wall(WallType type, int thickness) {
		this.type = type;
		this.thickness = thickness;
		setupCoords();
		WallManager.registerWall(this);
		collisionPredicate = ColisionDetector.getPredicateForRect(x, y, height,
				width);
	}

	private void setupCoords() {
		width = GameActivity.screenWidth;
		height = GameActivity.screenHeight;
		if (type.equals(WallType.LEFTWALL)) {
			x = 0;
			y = 0;
			width = thickness;
		} else if (type.equals(WallType.RIGHTWALL)) {
			x = GameActivity.screenWidth - thickness;
			y = 0;
			width = thickness;
		} else if (type.equals(WallType.BOTTOMWALL)) {
			x = 0;
			y = GameActivity.screenHeight - thickness;
			height = thickness;
		} else if (type.equals(WallType.TOPWALL)) {
			x = 0;
			y = 1;
			height = thickness;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public WallType getType() {
		return type;
	}

	public ColisionType colidesWith(Vector position) {
		return collisionPredicate.evaluate(position);
	}

	public int getColor() {
		return 0xffcccccc;
	}

}
