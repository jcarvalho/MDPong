package pt.ist.cm.game.ball;

public class Referential {

	public static final float DEFAULT_WIDTH = 320f, DEFAULT_HEIGHT = 480f;

	private static Referential instance;

	private int startX, startY, endX, endY;

	public static float xRatio = 1f, yRatio = 1f;

	public Referential(int startX, int startY, int endX, int endY) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	public static void setDeviceSize(float width, float height) {
		xRatio = width / DEFAULT_WIDTH;
		yRatio = height / DEFAULT_HEIGHT;
		System.out.println("X: " + xRatio + " Y: " + yRatio);
	}

	public static void setInstance(Referential ref) {
		instance = ref;
	}

	public static Referential getInstance() {
		return instance;
	}

	public void getActualPosition(Vector actualPosition, Vector position) {
		actualPosition.setX((position.getX() - startX) * xRatio);
		actualPosition.setY((position.getY() - startY) * yRatio);
	}

	/*
	 * 
	 */

	public void changeReferential(int cornerX, int cornerY, int width,
			int height) {
		this.startX = cornerX;
		this.startY = cornerY;
		this.endX = width;
		this.endY = height;
	}

	public boolean isInReferential(Vector vector) {
		float x = vector.getX(), y = vector.getY();
		return x >= startX && x <= endX && y >= startY && y <= endY;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public static void updatePosition(Vector actualPosition, Vector position) {
		instance.getActualPosition(actualPosition, position);
	}

	public Vector getMidPosition() {
		float x = (DEFAULT_WIDTH / 2);
		float y = (DEFAULT_HEIGHT / 2);
		return new Vector(x, y);
	}
}
