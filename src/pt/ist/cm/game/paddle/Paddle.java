package pt.ist.cm.game.paddle;

import pt.ist.cm.game.ColisionDetector;
import pt.ist.cm.game.ColisionPredicate;
import pt.ist.cm.game.ColisionType;
import pt.ist.cm.game.GameActivity;
import pt.ist.cm.game.ball.Vector;
import pt.ist.cm.game.player.Player;
import pt.ist.cm.game.player.PlayerListener;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.game.wall.Wall.WallType;
import pt.ist.cm.hardware.ScreenOrientation;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.util.Log;

public class Paddle implements PlayerListener {

	private static final int STEP = 6;
	private static final int DEFAULT_THICKNESS = 2;
	private static final int DEFAULT_LENGTH = 15; // Percentage of screen

	public static Paddle instance;
	private final WallType type;
	private ColisionPredicate collisionPredicate;

	private final int thickness = DEFAULT_THICKNESS;
	private final int length = DEFAULT_LENGTH;

	private final int leftLimit = 1;
	private final int rightLimit = GameActivity.screenWidth;
	// ScreenOrientation.getScreenWidth();
	private final int topLimit = GameActivity.screenHeight;
	// ScreenOrientation.getScreenHeight();

	private int width = ScreenOrientation.getScreenWidth() / 100 * length;
	private int height = ScreenOrientation.getScreenHeight() / 100 * thickness;
	private int x;
	private int y;
	private boolean dead = false;

	public Paddle(WallType type) {
		this.type = type;
		setupCoords();

		Log.d(this.getClass().getSimpleName(), "Creating paddle with width: "
				+ width + " and height: " + height + " and Y: " + y
				+ ". Initial X is: " + x + "RightLimit: " + rightLimit
				+ " and TopLimit: " + topLimit);

		instance = this;
		collisionPredicate = ColisionDetector.getPredicateForRect(x, y, height,
				width);

		NotificationManager.addListenerForEvent(this, EventType.PLAYER_LOST);

	}

	private void setupCoords() {
		Log.d(this.getClass().getSimpleName(), type.toString());
		if (ScreenOrientation.isLandscape()) { // Swap width and height when in
												// landscape mode
			this.width = ScreenOrientation.getScreenHeight() / 100 * thickness;
			this.height = ScreenOrientation.getScreenWidth() / 100 * length;
		}

		if (type == WallType.TOPWALL) { // Top
			this.x = ((rightLimit - leftLimit) / 2) - (width / 2);
			this.y = height;

		} else if (type == WallType.RIGHTWALL) { // Right
			this.x = rightLimit - width;
			this.y = topLimit / 2;

		} else if (type == WallType.BOTTOMWALL) { // Bottom
			this.x = ((rightLimit - leftLimit) / 2);
			this.y = topLimit - height;

		} else if (type == WallType.LEFTWALL) { // Left
			this.x = leftLimit + width;
			this.y = topLimit / 2;
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

	public void movePaddle(int direction) {

		int newPos;
		if (ScreenOrientation.isLandscape()) {
			newPos = nextPosition(this.y, direction, 0, topLimit - height);
			if (newPos != this.y) {
				this.y = newPos;
				if (!dead)
					collisionPredicate = ColisionDetector.getPredicateForRect(
							x, y, height, width);
			}
		} else {
			newPos = nextPosition(this.x, direction, leftLimit, rightLimit
					- width);
			if (newPos != this.x) {
				this.x = newPos;
				if (!dead)
					collisionPredicate = ColisionDetector.getPredicateForRect(
							x, y, height, width);
			}
		}
	}

	private int nextPosition(int oldPos, int direction, int lowerLimit,
			int upperLimit) {

		if (ScreenOrientation.getCurrentOrientation() == ScreenOrientation.LANDSCAPE_RIGHT
				|| ScreenOrientation.getCurrentOrientation() == ScreenOrientation.PORTRAIT_TOP)
			direction *= -1;
		// direction *=
		// ScreenOrientation.getCurrentOrientation().getReferential();
		int newPosition = oldPos + (direction * STEP);

		if (newPosition < lowerLimit) {
			newPosition = lowerLimit;
		} else if (newPosition > upperLimit) {
			newPosition = upperLimit;
		}

		return newPosition;
	}

	public ColisionType collidesWith(Vector position) {
		return collisionPredicate.evaluate(position);
	}

	public void convertToWall() {
		if (ScreenOrientation.isLandscape()) {
			collisionPredicate = ColisionDetector.getPredicateForRect(x, 0,
					GameActivity.screenHeight, width);
		} else {
			collisionPredicate = ColisionDetector.getPredicateForRect(0, y,
					height, GameActivity.screenWidth);
		}
	}

	/*
	 * PlayerListener compliance. We need to listen to the event when the
	 * current player loses, in order to turn the paddle into an invisible wall
	 */

	public void playerJoined(Player p) {

	}

	public void playerLeft(Player p) {

	}

	public void playerScored(Player p) {

	}

	public void playerLost(Player p) {
		if (PlayerManager.getCurrentPlayer().equals(p)) {
			convertToWall();
			this.dead = true;
		}
	}

	public void playerWon(Player p) {

	}

}
