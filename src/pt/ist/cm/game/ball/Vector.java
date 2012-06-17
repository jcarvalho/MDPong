package pt.ist.cm.game.ball;

import java.util.Random;

public class Vector {

	private float x, y;

	public Vector() {

	}

	public Vector(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void addTo(Vector v) {
		x += v.getX();
		y += v.getY();
	}

	public void reverseX() {
		x = -x;
	}

	public void reverseY() {
		y = -y;
	}

	public void increase(float d) {
		x += x < 0 ? -d : d;
		y += y < 0 ? -d : d;
	}

	@Override
	public String toString() {
		return "X: " + x + " Y: " + y;
	}

	public Vector randomize() {

		Random random = new Random();

		this.x += (random.nextBoolean() ? 1 : -1) * (random.nextInt(40));
		this.y += (random.nextBoolean() ? 1 : -1) * (random.nextInt(40));

		return this;
	}

}
