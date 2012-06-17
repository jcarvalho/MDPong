package pt.ist.cm.game;

import pt.ist.cm.game.ball.Ball;
import pt.ist.cm.game.ball.BallCreationTask;
import pt.ist.cm.game.ball.BallListener;
import pt.ist.cm.game.ball.BallManager;
import pt.ist.cm.game.ball.Referential;
import pt.ist.cm.game.dto.PlayerLeftDTO;
import pt.ist.cm.game.paddle.PaddleView;
import pt.ist.cm.game.player.MiniMapView;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.game.player.PlayerView;
import pt.ist.cm.game.wall.Neighbor;
import pt.ist.cm.game.wall.NeighborView;
import pt.ist.cm.game.wall.Pit;
import pt.ist.cm.game.wall.Wall.WallType;
import pt.ist.cm.game.wall.WallManager;
import pt.ist.cm.game.wall.WallView;
import pt.ist.cm.hardware.KeyListener;
import pt.ist.cm.hardware.ScreenOrientation;
import pt.ist.cm.hardware.SensorMapper;
import pt.ist.cm.network.ConnectionManager;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class GameActivity extends Activity implements KeyEventListener,
		BallListener {

	private SensorMapper sensorMapper;

	private PaddleView paddleView;

	private RelativeLayout containerLayout;

	private long lastTouchEvent = 0;

	private static final long TOUCH_EVENT_THRESHOLD = 150;

	private PlayerView playerView;

	private final int numOfBalls = 1;

	public static int screenWidth;
	public static int screenHeight;

	public static GameActivity currentActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentActivity = this;

		WallManager.clear();

		// Environment Setup

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.sensorMapper = new SensorMapper(this);
		this.containerLayout = new RelativeLayout(this);
		Display defaultDisplay = getWindowManager().getDefaultDisplay();

		screenWidth = defaultDisplay.getWidth();
		screenHeight = defaultDisplay.getHeight();

		Referential.setDeviceSize(screenWidth, screenHeight);

		// Elements

		PlayerConfiguration playerConfig = ScreenConfiguration
				.getPlayerConfig(PlayerManager.playerId);

		int currentwall = 0;

		for (ScreenInformation si : playerConfig.getScreenDefs()) {
			if (si == ScreenInformation.PADDLE) {
				this.paddleView = new PaddleView(this,
						convertIntToWallType(currentwall),
						ScreenOrientation.getCurrentOrientation());
				new Pit(convertIntToWallType(currentwall));
				this.containerLayout.addView(paddleView);
			} else if (si == ScreenInformation.WALL) {
				this.containerLayout.addView(new WallView(this,
						convertIntToWallType(currentwall)));
			} else if (si == ScreenInformation.NEIGHBOR) {
				Neighbor neighbor = new Neighbor(
						convertIntToWallType(currentwall));
				this.containerLayout.addView(new NeighborView(this, neighbor));
			}
			currentwall++;
		}

		// Stuff
		if (PlayerManager.isMasterPlayer()) {
			for (int i = 0; i < numOfBalls; i++) {
				addBall(BallManager.createNewBall());

			}
		} else {

			for (Ball ball : BallManager.getBalls()) {
				addBall(ball);
			}

		}

		this.playerView = new PlayerView(this);
		this.containerLayout.addView(playerView);
		this.containerLayout.addView(new MiniMapView(this));

		// Set container as main view

		if (PlayerManager.isMasterPlayer()) {
			new BallCreationTask().execute();
		}

		setContentView(containerLayout);
	}

	private WallType convertIntToWallType(int currentwall) {
		switch (currentwall) {
		case 0: {
			return WallType.LEFTWALL;
		}
		case 1: {
			return WallType.TOPWALL;
		}
		case 2: {
			return WallType.RIGHTWALL;
		}
		case 3: {
			return WallType.BOTTOMWALL;
		}
		}
		;
		return null;

	}

	public void addBall(final Ball ball) {
		runOnUiThread(new Runnable() {

			public void run() {
				if (BallManager.isRunning())
					ball.start();
				GameActivity.this.containerLayout.addView(BallManager
						.getViewForBall(GameActivity.this, ball.getId()));
			}
		});

	}

	/*
	 * Paddle Control
	 */

	public void keyPressed(KeyDirection direction, Class<?> sender) {
		if (direction.equals(KeyDirection.LEFT))
			paddleView.updatePaddle(-1);
		if (direction.equals(KeyDirection.RIGHT))
			paddleView.updatePaddle(1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getEventTime() - lastTouchEvent < TOUCH_EVENT_THRESHOLD) {
			return true;
		}

		lastTouchEvent = event.getEventTime();

		BallManager.startAllBalls();

		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		NotificationManager.addListenerForEvent(this, EventType.KEY_EVENT);
		NotificationManager.addListenerForEvent(this,
				EventType.BALL_CREATED_EVENT);
		this.sensorMapper.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		BallManager.clear();
		NotificationManager.removeListener(this, EventType.KEY_EVENT);
		NotificationManager.removeListener(this, EventType.BALL_CREATED_EVENT);
		this.sensorMapper.stop();
		currentActivity = null;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		KeyListener.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			if (this.sensorMapper.isRunning())
				this.sensorMapper.stop();
			else
				this.sensorMapper.start();
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConnectionManager.broadcast(new PlayerLeftDTO(
					PlayerManager.playerId));
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * BallListener compliance.
	 */

	public void ballFell(Ball ball) {
	}

	public void ballEntry(Ball ball) {
	}

	public void ballEntering(Ball event) {
	}

	public void ballCreated(Ball ball) {
		addBall(ball);
	}

}
