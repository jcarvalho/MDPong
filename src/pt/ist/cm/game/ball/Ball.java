package pt.ist.cm.game.ball;

import java.util.Random;

import pt.ist.cm.game.ColisionType;
import pt.ist.cm.game.ScreenConfiguration;
import pt.ist.cm.game.paddle.Paddle;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.game.wall.WallManager;
import pt.ist.cm.util.CountDownTimer;
import android.util.Log;

public class Ball {

	private transient static final Random random = new Random();

	private final String id;

	private Vector position = new Vector();

	private Vector direction;

	private transient volatile CountDownTimer timer;

	private volatile boolean isMoving = false;

	private volatile boolean isDead = false;

	public Ball(String id) {
		this.id = id;
		int rndPlayer = random.nextInt(PlayerManager.getNumPlayers());
		Referential rndPlayerReferential = ScreenConfiguration.getPlayerConfig(
				"" + rndPlayer).getReferential();
		Vector midPosition = rndPlayerReferential.getMidPosition().randomize();
		midPosition.addTo(new Vector(random.nextInt(20), random.nextInt(20)));

		setRandomBall(midPosition, random.nextBoolean() ? new Vector(-2, -2)
				: new Vector(-2, 2));
	}

	public Ball(String id, Vector position, Vector direction) {
		this.id = id;
		this.position = position;
		this.direction = direction;
		BallManager.registerBall(this);
	}

	private void setRandomBall(Vector position, Vector direction) {
		this.position = position;
		this.direction = direction;
		BallManager.registerBall(this);
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

	public void setPosition(Vector position) {
		this.position = position;
	}

	public void move() {

		if (!isMoving || isDead) {
			System.out.println("Trying to move a stopped ball: " + id);
			return;
		}

		Vector pos = this.position;
		pos.addTo(direction);

		ColisionType colision = Paddle.instance.collidesWith(pos);

		if (colision == ColisionType.NONE) {
			colision = WallManager.collidesWithAnyWall(pos);
		}

		if (colision == ColisionType.HORIZONTAL) {
			Log.d(this.getClass().getName(), "Horizontal colision found!");
			direction.reverseY();
			this.position.addTo(direction);
		} else if (colision == ColisionType.VERTICAL) {
			Log.d(this.getClass().getName(), "Vertical colision found!");
			direction.reverseX();
			this.position.addTo(direction);
		} else if (colision == ColisionType.PIT) {
			Log.d(this.getClass().getName(), "Ball fell down the pit. " + id);
			if (isMoving)
				BallManager.ballFell(PlayerManager.playerId, this.getId());
			this.kill();

		} else if (colision == ColisionType.NEIGHBOR) {

			BallManager.ballIsAboutToChange(this);

			Log.d(this.getClass().getName(), "Almost leaving area...");
		}

		if (!Referential.getInstance().isInReferential(pos)) {

			Log.d(this.getClass().getName(), "Ball left screen..." + this.id);

			this.kill();

			BallManager.ballLeftScreen(this);

			return;
		}

		this.position = pos;

		// this.direction.increase(0.005f);

		BallManager.ballMoved(this);
	}

	public void start() {
		System.out.println("Starting ball...");

		if (isDead) {
			return;
		}

		timer = new CountDownTimer(Long.MAX_VALUE, 50) {

			@Override
			public void onTick(long millisUntilFinished) {
				Ball.this.move();
				/*
				 * if (!isMoving || isDead) { new AsyncTask<Void, Void, Void>()
				 * {
				 * 
				 * @Override protected Void doInBackground(Void... params) { if
				 * (timer != null) timer.cancel(); timer = null; return null; }
				 * 
				 * }.execute(); }
				 */
			}

			@Override
			public void onFinish() {
				Ball.this.isMoving = false;
				timer = null;
			}
		};

		isMoving = true;
		timer.start();
	}

	public void stop() {
		isMoving = false;
		if (timer != null)
			timer.cancel();
		System.out.println("Stopping ball...");
	}

	public boolean isMoving() {
		return isMoving;
	}

	public boolean isDead() {
		return isDead;
	}

	public void kill() {
		this.stop();
		this.isDead = true;
	}

	public void setDirection(Vector direction) {
		this.direction = direction;
	}

	public void revive() {
		isDead = false;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Ball) {
			Ball ball = (Ball) o;
			return this.id.equals(ball.getId());
		}
		return false;

	}

}
